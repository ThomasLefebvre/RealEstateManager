package fr.thomas.lefebvre.realestatemanager.ui.screen.map

import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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
import fr.thomas.lefebvre.realestatemanager.database.dao.MediaDAO
import fr.thomas.lefebvre.realestatemanager.database.dao.PropertyDAO
import fr.thomas.lefebvre.realestatemanager.databinding.DetailsFragmentBinding
import fr.thomas.lefebvre.realestatemanager.databinding.FragmentMapBinding
import fr.thomas.lefebvre.realestatemanager.databinding.PropertyFragmentBinding
import fr.thomas.lefebvre.realestatemanager.ui.screen.detailsProperty.DetailsViewModel
import fr.thomas.lefebvre.realestatemanager.ui.screen.listProperty.PropertyViewModel
import fr.thomas.lefebvre.realestatemanager.ui.screen.listProperty.PropertyViewModelFactory

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [MapFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [MapFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MapFragment : Fragment(), OnMapReadyCallback,GoogleMap.OnMarkerClickListener,GoogleMap.OnInfoWindowClickListener{



    private lateinit var database: PropertyDAO


    private lateinit var viewModel: PropertyViewModel

    private lateinit var mMapVIew: MapView


    lateinit var mMap:GoogleMap
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient


    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }

    var listId=ArrayList<Long>()



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding: FragmentMapBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_map, container, false)


        fusedLocationProviderClient= LocationServices.getFusedLocationProviderClient(requireContext())


        //init the service map
        mMapVIew = binding.mapViewMap
        mMapVIew.onCreate(null)
        mMapVIew.onResume()
        mMapVIew.getMapAsync(this)


        val application = requireNotNull(this.activity).application


        database = PropertyDatabase.getInstance(application).propertyDAO


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


    override fun onMapReady(googleMap: GoogleMap?) {
        MapsInitializer.initialize(context)
        mMap = googleMap!!

        setUpMap()

        loadProperty()
        mMap.setOnInfoWindowClickListener(this)
    }

    override fun onMarkerClick(propertyMarker: Marker?): Boolean {
        return false
    }

    override fun onInfoWindowClick(propertyMarker: Marker?) {
        Log.d("DEBUG",propertyMarker!!.title)
        var index=propertyMarker.zIndex.toInt()
        val isLarge: Boolean = resources.getBoolean(R.bool.isLarge)
        if (isLarge) {
            viewModel.changeIdProperty(listId[index])
        } else {
            viewModel.changeIdProperty(listId[index])
            view!!.findNavController().navigate(R.id.detailsFragment)
        }
    }

    private fun setUpMap() {
        if (ActivityCompat.checkSelfPermission(requireContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
            return
        }



        mMap.isMyLocationEnabled=true

        fusedLocationProviderClient.lastLocation.addOnSuccessListener { location ->
            if(location!=null){
                val currentLocation= LatLng(location.latitude,location.longitude)
                mMap.moveCamera(CameraUpdateFactory.newLatLng(currentLocation))
            }
        }
    }

    private fun loadProperty(){
        viewModel.listProperty.observe(this, Observer { propertys->
            createMarkerOfProperty(propertys)

        })
    }

    private fun createMarkerOfProperty(listProperty:List<Property>){
        var i:Float =0F
        listProperty.forEach { property ->

            Log.d("DEBUG",property.idProperty.toString())
            listId.add(property.idProperty)
            if (property.lat!=-1.0&&property.lng!=-1.0){
                val location= LatLng(property.lat!!,property.lng!!)
                mMap.addMarker(MarkerOptions().position(location).title(property.address).zIndex(i))
                i += 1
            }
        }
    }



    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
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
