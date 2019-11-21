package fr.thomas.lefebvre.realestatemanager.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.onNavDestinationSelected
import androidx.navigation.ui.setupWithNavController
import fr.thomas.lefebvre.realestatemanager.R
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.myNavHostFragment) as NavHostFragment?


        val navController = findNavController(R.id.myNavHostFragment)
        appBarConfiguration = AppBarConfiguration(navController.graph)



        NavigationUI.setupWithNavController(bottomNavigationView, navHostFragment!!.navController)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return super.onOptionsItemSelected(item)|| item.onNavDestinationSelected(findNavController(R.id.myNavHostFragment))
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_options, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onBackPressed() {
        alertDialogBoxLeave()
    }

    fun alertDialogBoxLeave(){
        val alertDialog=AlertDialog.Builder(this)
        alertDialog.setTitle(R.string.dialog_leave_title)
        alertDialog.setMessage(R.string.dialog_leave_message)
        alertDialog.setPositiveButton(R.string.dialog_yes){dialogInterface, i ->
            super.onBackPressed()
        }
        alertDialog.setNegativeButton(R.string.dialog_no){dialogInterface, i ->  }
        alertDialog.show()
    }
}
