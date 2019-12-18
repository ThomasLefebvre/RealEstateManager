package fr.thomas.lefebvre.realestatemanager.ui.screen.detailsProperty

import android.app.Application
import android.content.Intent
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import fr.thomas.lefebvre.realestatemanager.R
import fr.thomas.lefebvre.realestatemanager.database.PropertyDatabase
import fr.thomas.lefebvre.realestatemanager.database.dao.MediaDAO
import fr.thomas.lefebvre.realestatemanager.database.dao.PropertyDAO
import fr.thomas.lefebvre.realestatemanager.databinding.DetailsFragmentBinding
import fr.thomas.lefebvre.realestatemanager.ui.screen.editProperty.EditActivity
import fr.thomas.lefebvre.realestatemanager.ui.screen.listProperty.PropertyViewModel
import fr.thomas.lefebvre.realestatemanager.ui.screen.listProperty.PropertyViewModelFactory
import kotlinx.android.synthetic.main.details_fragment.*


class DetailsFragment : Fragment(), OnMapReadyCallback {


    private lateinit var viewModel: DetailsViewModel

    private lateinit var databaseProperty: PropertyDAO

    private lateinit var databaseMedia: MediaDAO

    private lateinit var viewModelProperty: PropertyViewModel


    private lateinit var gmap: GoogleMap
    private lateinit var mMapVIew: MapView

    private lateinit var binding:DetailsFragmentBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
         binding=
            DataBindingUtil.inflate(inflater, R.layout.details_fragment, container, false)

        //init the service map
        mMapVIew = binding.mapView
        mMapVIew.onCreate(null)
        mMapVIew.onResume()
        mMapVIew.getMapAsync(this)


        val application = requireNotNull(this.activity).application


        databaseProperty = PropertyDatabase.getInstance(application).propertyDAO
        databaseMedia = PropertyDatabase.getInstance(application).mediaDAO


        //init the property view model
        val viewModelFactoryProperty = PropertyViewModelFactory(databaseProperty, application)
        viewModelProperty = activity!!.run {
            ViewModelProviders.of(this, viewModelFactoryProperty).get(PropertyViewModel::class.java)
        }


        //init the details view model
        val viewModelFactory =
            DetailsViewModelFactory(
                databaseProperty,
                databaseMedia,
                application

            )
        viewModel =
            ViewModelProviders.of(this, viewModelFactory).get(DetailsViewModel::class.java)

        binding.detailsFragmentViewModel = viewModel

        binding.lifecycleOwner = this

        onClickEdit()


        return binding.root
    }

    override fun onMapReady(googleMap: GoogleMap?) {//init the map
        MapsInitializer.initialize(context)
        gmap = googleMap!!
        //observe the position of property and get this
        viewModel.latLng.observe(this, Observer { latLng ->
                gmap.moveCamera(CameraUpdateFactory.newLatLng(latLng))//move camera to property
                googleMap.addMarker(MarkerOptions().position(latLng))//add marker to property
        })


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {


        //if tab update the idProperty with observable liveData on the property view model
        viewModelProperty.idProperty.observe(this, Observer { idProperty ->
            viewModel.initPropertyDetails(idProperty)
            viewModel.initMedia(idProperty)
        })


        setRecyclerViewPhoto()
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onResume() {//update information if edit property
        //if tab update the idProperty with observable liveData on the property view model
        viewModelProperty.idProperty.observe(this, Observer { idProperty ->
            viewModel.initPropertyDetails(idProperty)
            viewModel.initMedia(idProperty)
            

        })
        setRecyclerViewPhoto()
        super.onResume()
    }


   private fun setRecyclerViewPhoto() {
        val layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)


        recycler_view_photo_details.layoutManager = layoutManager

        viewModel.listMedia.observe(this, Observer { medias ->
            if (medias.isNotEmpty()) {
                recycler_view_photo_details.adapter = DetailsPhotoAdapter(medias)
                recycler_view_photo_details.visibility = View.VISIBLE
                textViewNoPhoto.visibility = View.GONE
            } else {
                textViewNoPhoto.visibility = View.VISIBLE
                recycler_view_photo_details.visibility = View.GONE
            }
        })
    }

    private fun onClickEdit(){
            binding.floatingActionButtonEdit.setOnClickListener {
                val intentEdit= Intent(requireContext(),EditActivity::class.java)
                intentEdit.putExtra("idProperty",viewModel.property.value!!.idProperty)
                startActivity(intentEdit)


            }
    }




}
