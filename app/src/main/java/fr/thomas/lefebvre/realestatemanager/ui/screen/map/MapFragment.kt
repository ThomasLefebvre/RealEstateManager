package fr.thomas.lefebvre.realestatemanager.ui.screen.map

import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import fr.thomas.lefebvre.realestatemanager.R
import fr.thomas.lefebvre.realestatemanager.database.Property
import fr.thomas.lefebvre.realestatemanager.database.PropertyDatabase
import fr.thomas.lefebvre.realestatemanager.database.dao.PropertyDAO
import fr.thomas.lefebvre.realestatemanager.databinding.FragmentMapBinding
import fr.thomas.lefebvre.realestatemanager.ui.screen.listProperty.PropertyViewModel
import fr.thomas.lefebvre.realestatemanager.ui.screen.listProperty.PropertyViewModelFactory


class MapFragment : Fragment(), OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener {


    private lateinit var database: PropertyDAO
    private lateinit var viewModel: PropertyViewModel
    private lateinit var mMapVIew: MapView
    private lateinit var mMap: GoogleMap
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient


    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }

    var listId = ArrayList<Long>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding: FragmentMapBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_map, container, false)


        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireContext())


        //init the service map
        mMapVIew = binding.mapViewMap
        mMapVIew.onCreate(null)
        mMapVIew.onResume()
        mMapVIew.getMapAsync(this)


        val application = requireNotNull(this.activity).application//init application
        database = PropertyDatabase.getInstance(application).propertyDAO//init database

        //init view model
        val viewModelFactory =
            PropertyViewModelFactory(
                database,
                application
            )
        viewModel = activity!!.run {
            ViewModelProviders.of(this, viewModelFactory).get(PropertyViewModel::class.java)
        }

        binding.lifecycleOwner = this

        return binding.root
    }


    override fun onMapReady(googleMap: GoogleMap?) {//initialize map and load property
        MapsInitializer.initialize(context)
        mMap = googleMap!!
        setUpMap()
        loadProperty()
        mMap.setOnInfoWindowClickListener(this)
    }


    override fun onInfoWindowClick(propertyMarker: Marker?) {//manage the click on info window marker
        Log.d("DEBUG", propertyMarker!!.title)
        var index = propertyMarker.zIndex.toInt()
        val isLarge: Boolean = resources.getBoolean(R.bool.isLarge)//init boolean tablet or mobile
        if (isLarge) {//if tablet
            viewModel.changeIdProperty(listId[index])//update id property view model
        } else { //if mobile
            viewModel.changeIdProperty(listId[index])//update id property view model
            view!!.findNavController()
                .navigate(R.id.action_fragmentMap_to_detailsFragment)//launch details fragment
        }
    }

    private fun setUpMap() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),//check the permission location
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {//if don't request permission
            requestPermissions(
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
            return
        }

        mMap.isMyLocationEnabled = true//enable location service

        fusedLocationProviderClient.lastLocation.addOnSuccessListener { location ->
            //get the last location of user
            if (location != null) {
                val currentLocation = LatLng(location.latitude, location.longitude)
                mMap.moveCamera(CameraUpdateFactory.newLatLng(currentLocation))
            }
        }
    }

    private fun loadProperty() {//load property in view model and observe if ready for create marker
        viewModel.listProperty.observe(this, Observer { propertys ->
            //observe list property from view model
            createMarkerOfProperty(propertys)

        })
    }

    private fun createMarkerOfProperty(listProperty: List<Property>) {//create marker of property
        var i: Float = 0F
        listProperty.forEach { property ->

            Log.d("DEBUG", property.idProperty.toString())
            listId.add(property.idProperty)//add property on local list for recover id on the info windows click with index
            if (property.lat != -1.0 && property.lng != -1.0) {
                val location = LatLng(property.lat!!, property.lng!!)
                mMap.addMarker(MarkerOptions().position(location).title(property.address).zIndex(i))//set the index for recover id property
                i += 1
            }
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            LOCATION_PERMISSION_REQUEST_CODE -> {
                // If request is cancelled, the result arrays are empty.
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    setUpMap()
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return
            }
            else -> {
                // Ignore all other requests.
            }
        }
    }


}
