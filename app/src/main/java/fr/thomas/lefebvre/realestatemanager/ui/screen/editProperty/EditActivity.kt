package fr.thomas.lefebvre.realestatemanager.ui.screen.editProperty

import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.DatePicker
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import fr.thomas.lefebvre.realestatemanager.R
import fr.thomas.lefebvre.realestatemanager.database.PropertyDatabase
import fr.thomas.lefebvre.realestatemanager.database.dao.MediaDAO
import fr.thomas.lefebvre.realestatemanager.database.dao.PropertyDAO
import fr.thomas.lefebvre.realestatemanager.databinding.ActivityEditBinding
import fr.thomas.lefebvre.realestatemanager.ui.screen.addProperty.AddPropertyActivity
import fr.thomas.lefebvre.realestatemanager.ui.screen.detailsProperty.DetailsViewModel
import fr.thomas.lefebvre.realestatemanager.ui.screen.detailsProperty.DetailsViewModelFactory
import fr.thomas.lefebvre.realestatemanager.util.formatDateLongToString
import kotlinx.android.synthetic.main.activity_add_property.*
import kotlinx.android.synthetic.main.activity_add_property.buttonSelectAddress
import kotlinx.android.synthetic.main.activity_edit.*
import java.util.*

class EditActivity : AppCompatActivity(), DatePickerDialog.OnDateSetListener {


    private lateinit var viewModel: DetailsViewModel

    private lateinit var databaseProperty: PropertyDAO

    private lateinit var databaseMedia: MediaDAO

    lateinit var binding: ActivityEditBinding


    companion object {
        //CODE REQUESTS
        private const val AUTOCOMPLETE_REQUEST = 100

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_edit)

        val idProperty = intent.getLongExtra("idProperty", 0)

        databaseProperty = PropertyDatabase.getInstance(application).propertyDAO
        databaseMedia = PropertyDatabase.getInstance(application).mediaDAO

        // Initialize the SDK
        Places.initialize(applicationContext, getString(R.string.api_key_maps))


        //init the details view model
        val viewModelFactory =
            DetailsViewModelFactory(
                databaseProperty,
                databaseMedia,
                application

            )
        viewModel =
            ViewModelProviders.of(this, viewModelFactory).get(DetailsViewModel::class.java)

        binding.editViewModel = viewModel

        binding.lifecycleOwner = this

        viewModel.initPropertyEdit(idProperty)
        viewModel.initMedia(idProperty)

        onClickButtonAddAddress()

        onClickButtonUpdate(idProperty)

        onClickSwitchSold()

        setTitle(getString(R.string.edit_title))
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)



    }

    override fun onSupportNavigateUp(): Boolean {//set action with click on support action bar
        onBackPressed()
        return true
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {


            AUTOCOMPLETE_REQUEST -> {
                if (resultCode == Activity.RESULT_OK) {
                    val place = Autocomplete.getPlaceFromIntent(data!!)
                    textAddressEdit.setText(place.address)
                    viewModel.updateLatLng(place.latLng!!.latitude, place.latLng!!.longitude)

                } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {//if error in request
                    val status = Autocomplete.getStatusFromIntent(data!!)
                    Log.i("DEBUG_AUTOCOMPLETE", status.statusMessage!!)
                }
            }
        }

        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun onClickButtonUpdate(idProperty: Long) {
        floatingActionButtonSaveEdit.setOnClickListener {
            viewModel.updateProperty(idProperty)
            finish()
        }
    }

    private fun onClickSwitchSold() {
        switchSold.setOnClickListener {
            if (switchSold.isChecked) {
                showDatePickerDialog()
            }
        }
    }

    private fun showDatePickerDialog() {
        val datePickerDialog = DatePickerDialog(//build datepicker dialog and
            this,
            this,
            Calendar.getInstance().get(Calendar.YEAR),
            Calendar.getInstance().get(Calendar.MONTH),
            Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.show()//show date picker dialog

        datePickerDialog.setOnCancelListener {//if click on cancel button, swtich the button switch to false (no date)
            switchSold.isChecked = false
        }
    }

    override fun onDateSet(p0: DatePicker?, year: Int, month: Int, day: Int) {
        val calendar = GregorianCalendar(year, month, day)
        val dateSold = calendar.timeInMillis
        Toast.makeText(this, formatDateLongToString(dateSold), Toast.LENGTH_LONG).show()
        viewModel.updateSaleDate(dateSold)
        textViewDate.setText(formatDateLongToString(dateSold))

    }


}
