package fr.thomas.lefebvre.realestatemanager.ui.screen.editProperty

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
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
import kotlinx.android.synthetic.main.activity_add_property.*
import kotlinx.android.synthetic.main.activity_add_property.buttonSelectAddress
import kotlinx.android.synthetic.main.activity_edit.*
import java.util.*

class EditActivity : AppCompatActivity() {


    private lateinit var viewModel: DetailsViewModel

    private lateinit var databaseProperty: PropertyDAO

    private lateinit var databaseMedia: MediaDAO

    lateinit var binding:ActivityEditBinding


    companion object {
        //CODE REQUESTS
        private const val AUTOCOMPLETE_REQUEST = 100

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=DataBindingUtil.setContentView(this,R.layout.activity_edit)

        val idProperty=intent.getLongExtra("idProperty",0)

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
                   viewModel.updateLatLng(place.latLng!!.latitude,place.latLng!!.longitude)
                    Log.d("LATLNG", viewModel.lat.value.toString())
                } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {//if error in request
                    val status = Autocomplete.getStatusFromIntent(data!!)
                    Log.i("DEBUG_AUTOCOMPLETE", status.statusMessage!!)
                }
            }
        }

        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun onClickButtonUpdate(idProperty:Long){
        floatingActionButtonSaveEdit.setOnClickListener {
            viewModel.updateProperty(idProperty)
            onBackPressed()
        }
    }
}
