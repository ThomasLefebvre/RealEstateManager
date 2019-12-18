package fr.thomas.lefebvre.realestatemanager.ui.activity


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.AppBarConfiguration
import fr.thomas.lefebvre.realestatemanager.R
import fr.thomas.lefebvre.realestatemanager.databinding.ActivityMainBinding



class MainActivity : AppCompatActivity() {


    private lateinit var appBarConfiguration: AppBarConfiguration


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        @Suppress("UNUSED_VARIABLE")
        val binding =
            DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)

        val navController = findNavController(R.id.myNavHostFragment)
        //set up action bar with onBackPressed
        NavigationUI.setupActionBarWithNavController(this, navController)
        appBarConfiguration = AppBarConfiguration.Builder(navController.graph)
            .build()


    }
    //set up action bar with onBackPressed
    override fun onSupportNavigateUp(): Boolean {
        val navController = this.findNavController(R.id.myNavHostFragment)
        return NavigationUI.navigateUp(navController, appBarConfiguration)
    }


}
