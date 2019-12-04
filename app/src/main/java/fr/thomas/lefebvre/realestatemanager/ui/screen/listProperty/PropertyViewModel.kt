package fr.thomas.lefebvre.realestatemanager.ui.screen.listProperty

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel;
import fr.thomas.lefebvre.realestatemanager.database.Property
import fr.thomas.lefebvre.realestatemanager.database.PropertyDatabase
import fr.thomas.lefebvre.realestatemanager.database.dao.MediaDAO
import fr.thomas.lefebvre.realestatemanager.database.dao.PropertyDAO
import kotlinx.coroutines.*

class PropertyViewModel(
    val database: PropertyDAO,
    application: Application
) : AndroidViewModel(
    application
) {

   internal val listProperty=database.getAllProperty()







}
