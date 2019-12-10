package fr.thomas.lefebvre.realestatemanager.ui.screen.map

import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import fr.thomas.lefebvre.realestatemanager.R
import fr.thomas.lefebvre.realestatemanager.database.Property
import fr.thomas.lefebvre.realestatemanager.database.PropertyDatabase
import fr.thomas.lefebvre.realestatemanager.database.dao.PropertyDAO
import fr.thomas.lefebvre.realestatemanager.ui.screen.listProperty.PropertyViewModel
import fr.thomas.lefebvre.realestatemanager.ui.screen.listProperty.PropertyViewModelFactory

class MapActivity : AppCompatActivity(){



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)


    }



}
