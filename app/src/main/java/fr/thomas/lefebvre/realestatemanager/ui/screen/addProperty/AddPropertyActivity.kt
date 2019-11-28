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
import fr.thomas.lefebvre.realestatemanager.R
import fr.thomas.lefebvre.realestatemanager.database.PropertyDatabase
import fr.thomas.lefebvre.realestatemanager.databinding.ActivityAddPropertyBinding
import kotlinx.android.synthetic.main.activity_add_property.*


class AddPropertyActivity : AppCompatActivity() {


    lateinit var viewModel: AddPropertyViewModel
    lateinit var binding: ActivityAddPropertyBinding

    lateinit var uriPhoto: Uri

    lateinit var mAdapter : PhotoAdapter

    companion object {
        const val CODE_CHOOSE_PHOTO = 200
        const val PHOTO_PERMISSION: Int = 100

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_property)

        val application = requireNotNull(this).application

        val database = PropertyDatabase.getInstance(application).propertyDAO
        val databaseAgent = PropertyDatabase.getInstance(application).agentDAO
        val databasePhoto=PropertyDatabase.getInstance(application).mediaDAO

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

        setRecyclerView()
    }


    fun onClickButtonSave() {

        binding.floatingActionButton.setOnClickListener {
            if (viewModel.listAgent.value!!.isNotEmpty() && allFieldInformed()) {
                val agentId =
                    viewModel.listAgent.value?.get(spinnerAgent.selectedItemPosition)?.idAgent
                val type = viewModel.listType.value!![spinnerType.selectedItemPosition]
                viewModel.onSaveProperty(agentId!!, type)
                super.onBackPressed()

            } else {
                Toast.makeText(this, R.string.complete_all_informations, Toast.LENGTH_LONG)
                    .show()

            }
        }

    }

    private fun allFieldInformed(): Boolean =
        (viewModel.editTextState.value != null
                && viewModel.editTextPrice.value != null
                && viewModel.editTextSurface.value != null
                && viewModel.editTextRoom.value != null
                && viewModel.editTextPostalCode.value != null
                && viewModel.editTextCity.value != null
                && viewModel.editTextNameWay.value != null
                && viewModel.editTextTypeWay.value != null
                && viewModel.editTextNumWay.value != null
                && viewModel.editTextDescription.value != null)


    fun onClickButtonAddPhoto() {
        binding.buttonAddPhoto.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (ActivityCompat.checkSelfPermission(
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
                    openGallery()
                }
            } else {
                //sdk is < 23
                openGallery()
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
                    //user choose a photo
                    this.uriPhoto = data!!.data!!
                    saveUriPhotoInDatabase(uriPhoto)
                    mAdapter.notifyDataSetChanged()
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun saveUriPhotoInDatabase(uriPhoto: Uri) {
        viewModel.addPhotoUriToList(uriPhoto)//save uri on the list in view model
    }




    private fun setRecyclerView() {//set recycler view on the onCreateMethode
            val layoutManager=LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)
            mAdapter= PhotoAdapter(viewModel.listUriPhoto) { position: Int, uri: Uri ->
                                articleClick(position,uri)
            }
            recycler_view_photo_propertie.layoutManager=layoutManager
            recycler_view_photo_propertie.adapter=mAdapter
    }

    private fun articleClick(position:Int,uri: Uri) {//method for remove the item on the clic
        viewModel.listUriPhoto.remove(uri)//remove item on the list uri
        mAdapter.notifyDataSetChanged()

    }
}
