package fr.thomas.lefebvre.realestatemanager.ui.screen.addProperty

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import fr.thomas.lefebvre.realestatemanager.database.PropertyDatabase
import fr.thomas.lefebvre.realestatemanager.databinding.ActivityAddPropertyBinding
import kotlinx.android.synthetic.main.activity_add_property.*
import java.util.*
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.net.PlacesClient
import android.R.attr.apiKey
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.icu.text.SimpleDateFormat
import android.os.Environment
import android.util.Log
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.DividerItemDecoration
import com.google.android.libraries.places.widget.AutocompleteActivity
import fr.thomas.lefebvre.realestatemanager.R
import java.io.File
import java.io.IOException
import kotlin.collections.ArrayList


class AddPropertyActivity : AppCompatActivity() {


    lateinit var viewModel: AddPropertyViewModel//viewmode
    lateinit var binding: ActivityAddPropertyBinding//data binding

    lateinit var uriPhoto: Uri //uri of the photo pick or take

    lateinit var mAdapter: PhotoAdapter// adapter for the photo recycler view

    lateinit var currentPhotoPath: String //name of the photo taken

    private val CHANNEL_ID = "fr.thomas.lefebvre.realestatemanager"//channel id for notification
    private val NOTIFICATION_ID = 0//notification id


    companion object {
        //CODE REQUESTS
        const val CODE_TAKE_PHOTO = 400
        const val CODE_CHOOSE_PHOTO = 300
        const val PHOTO_PERMISSION: Int = 200
        private const val AUTOCOMPLETE_REQUEST = 100

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_property)

        val application = requireNotNull(this).application

        // Initialize the SDK
        Places.initialize(applicationContext, getString(R.string.api_key_maps))

        // Create a new Places client instance
        val placesClient = Places.createClient(this)

        val database = PropertyDatabase.getInstance(application).propertyDAO
        val databaseAgent = PropertyDatabase.getInstance(application).agentDAO
        val databasePhoto = PropertyDatabase.getInstance(application).mediaDAO

        val viewModelFactory =
            AddViewModelFactory(
                database,
                databaseAgent,
                databasePhoto,
                application
            )

        setTitle(getString(R.string.add_property))

        viewModel =
            ViewModelProviders.of(this, viewModelFactory).get(AddPropertyViewModel::class.java)


        binding.addPropertyViewModel = viewModel
        binding.lifecycleOwner = this


        onClickButtonSave()
        onClickButtonAddPhoto()
        onClickButtonAddAddress()


        setRecyclerView()
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)


    }

    override fun onSupportNavigateUp(): Boolean {//set action with click on support action bar
        onBackPressed()
        return true
    }


    fun onClickButtonSave() {

        binding.floatingActionButtonSave.setOnClickListener {
            if (viewModel.listAgent.value!!.isNotEmpty()) {
                val agentId =
                    viewModel.listAgent.value?.get(spinnerAgent.selectedItemPosition)?.idAgent
                val type = viewModel.listType.value!![spinnerType.selectedItemPosition]
                viewModel.onSaveProperty(agentId!!, type, getDescriptionPhotoValue())
                showNotification(this)
                super.onBackPressed()

            } else {
                Toast.makeText(this, R.string.complete_all_informations, Toast.LENGTH_LONG)
                    .show()

            }
        }

    }


    fun onClickButtonAddPhoto() {

        binding.buttonAddPhoto.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {//check the version of sdk for permission access storage
                if (ActivityCompat.checkSelfPermission(//if sdk 23 or superior check permission and demand access
                        this,
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                    ) == PackageManager.PERMISSION_DENIED || ActivityCompat.checkSelfPermission(
                        this,
                        android.Manifest.permission.CAMERA
                    ) == PackageManager.PERMISSION_DENIED
                ) {
                    //permission was not enabled
                    val permission = arrayOf(
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        android.Manifest.permission.CAMERA
                    )
                    requestPermissions(
                        permission,
                        PHOTO_PERMISSION
                    )
                } else {
                    //permission already granted
                    alertDialogMedia()
                }
            } else {
                //sdk is < 23 not need permission access
                alertDialogMedia()
            }
        }
    }

    private fun openGallery() {

        val intentGallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(
            intentGallery,
            CODE_CHOOSE_PHOTO
        )

    }

    private fun openCamera() {

        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            // Ensure that there's a camera activity to handle the intent
            takePictureIntent.resolveActivity(packageManager)?.also {
                // Create the File where the photo should go
                val photoFile: File? = try {
                    createImageFile()
                } catch (ex: IOException) {
                    // Error occurred while creating the File
                    null
                }
                // Continue only if the File was successfully created
                photoFile?.also {
                    val photoURI: Uri = FileProvider.getUriForFile(
                        this,
                        "fr.thomas.lefebvre.realestatemanager.fileprovider",
                        it
                    )
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(takePictureIntent, CODE_TAKE_PHOTO)
                }
            }
        }
    }

    private fun galleryAddPic() {
        Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE).also { mediaScanIntent ->
            val f = File(currentPhotoPath)
            mediaScanIntent.data = Uri.fromFile(f)
            sendBroadcast(mediaScanIntent)
        }
    }


    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp: String = System.currentTimeMillis().toString()
        val storageDir: File = getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!
        return File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            currentPhotoPath = absolutePath
        }
    }

    private fun alertDialogMedia() {

        val builder = AlertDialog.Builder(this)
        // Set the alert dialog title
        builder.setTitle("Photo")
        // Display a message on alert dialog
        builder.setMessage("Take photo with camera or pick photo from gallery")
        // Set a positive button and its click listener on alert dialog
        builder.setPositiveButton("GALLERY") { dialog, which ->
            // Do something when user press the positive button
            openGallery()
            // Change the app background color
        }
        // Display a negative button on alert dialog
        builder.setNegativeButton("CAMERA") { dialog, which ->
            openCamera()
        }
        // Display a neutral button on alert dialog
        builder.setNeutralButton("Cancel") { _, _ ->
            Toast.makeText(applicationContext, "You cancelled the dialog.", Toast.LENGTH_SHORT)
                .show()
        }
        // Finally, make the alert dialog using builder
        val dialog: AlertDialog = builder.create()
        // Display the alert dialog on app interface
        dialog.show()
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        //called when user pressed ALLOW or DENY
        when (requestCode) {

            PHOTO_PERMISSION -> {
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //permission from popup was granted
                    openGallery()
                } else {
                    //permission from popup was denied
                    Toast.makeText(this, "Permission was denied", Toast.LENGTH_LONG)//TODO
                        .show()
                }
            }
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {

            CODE_CHOOSE_PHOTO -> {
                if (resultCode == Activity.RESULT_OK) {
                    Log.d("DEBUG", data!!.data!!.toString())
                    saveUriPhotoInDatabase(data!!.data!!)

                    mAdapter.notifyItemInserted(0)

                }
            }

            CODE_TAKE_PHOTO -> {
                if (resultCode == Activity.RESULT_OK) {
                    Log.d("DEBUG", Uri.parse(currentPhotoPath!!).toString())
                    saveUriPhotoInDatabase(Uri.parse("file://" + currentPhotoPath))
                    mAdapter.notifyItemInserted(0)
                    galleryAddPic()
                }
            }

            AUTOCOMPLETE_REQUEST -> {
                if (resultCode == Activity.RESULT_OK) {
                    val place = Autocomplete.getPlaceFromIntent(data!!)
                    textAddress.setText(place.address)
                    viewModel.lat.value = place.latLng!!.latitude
                    viewModel.lng.value = place.latLng!!.longitude
                    Log.d("LATLNG", viewModel.lat.value.toString())
                } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {//if error in request
                    val status = Autocomplete.getStatusFromIntent(data!!)
                    Log.i("DEBUG_AUTOCOMPLETE", status.statusMessage!!)
                }
            }
        }

        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun saveUriPhotoInDatabase(uriPhoto: Uri) {
        viewModel.addPhotoUriToList(uriPhoto)//save uri on the list in view model
    }


    private fun setRecyclerView() {//set recycler view on the onCreateMethode
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        mAdapter = PhotoAdapter(viewModel.listUriPhoto) { position: Int, uri: Uri ->
            articleClick(position, uri)
        }
        recycler_view_photo_propertie.addItemDecoration(
            DividerItemDecoration(
                this,
                DividerItemDecoration.VERTICAL
            )
        )
        recycler_view_photo_propertie.layoutManager = layoutManager
        recycler_view_photo_propertie.adapter = mAdapter
    }

    private fun articleClick(position: Int, uri: Uri) {//method for remove the item on the clic
        Toast.makeText(
            this,
            recycler_view_photo_propertie.childCount.toString() + " position: $position " + viewModel.listUriPhoto.size.toString(),
            Toast.LENGTH_LONG
        ).show()
        viewModel.listUriPhoto.removeAt(position)//remove item on the list uri
        mAdapter.notifyItemRemoved(position)


    }


    private fun initAutoCompleteIntent() {

        val fields = Arrays.asList(
            Place.Field.ID,
            Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG
        )//init the fields return from autocomplete
        val intentAutocomplete =
            Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fields)
                .build(this)
        startActivityForResult(intentAutocomplete, AUTOCOMPLETE_REQUEST)
    }

    private fun onClickButtonAddAddress() {
        buttonSelectAddress.setOnClickListener {
            initAutoCompleteIntent()
        }
    }

    private fun getDescriptionPhotoValue(): ArrayList<String> {//get description photo for save in data
        val listDescription = ArrayList<String>()
        for (i in 0 until recycler_view_photo_propertie.childCount) {//get the number of list
            val view = recycler_view_photo_propertie.layoutManager?.findViewByPosition(i)

            val stringDescription =
                view?.findViewById<EditText>(R.id.editTextDescriptionRv)?.text.toString()
            if (stringDescription != null) {
                listDescription.add(stringDescription)
            } else {
                listDescription.add("")
            }

        }
        return listDescription
    }

    @Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
    private fun createNotificationChannel(context: Context) {
        // Create  the channel
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Notification channel name"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance)
            channel.description = "Notification channel description"
            Log.i("ALARM RECEVEIR", "Create the channel in the system")
            // SAVE THE CHANNEL IN THE SYSTEM
            val notificationManager = context.getSystemService(NotificationManager::class.java)
            Objects.requireNonNull(notificationManager).createNotificationChannel(channel)
            Log.i("ALARM RECEVEIR", "Save the channel in the system")
        }
    }

    fun showNotification(context: Context) {
        //SET CHANNEL
        createNotificationChannel(context)
        //SET NOTIFICATION
        val notificationManager = NotificationManagerCompat.from(context)
        val notifBuilder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.house_ic)
            .setContentTitle(getString(R.string.title_notif))
            .setContentText(getString(R.string.body_notif))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        //SET SHOW NOTIFICATION
        notificationManager.notify(NOTIFICATION_ID, notifBuilder.build())


    }

}
