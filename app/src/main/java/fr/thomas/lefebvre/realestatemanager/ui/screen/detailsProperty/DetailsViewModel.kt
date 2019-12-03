package fr.thomas.lefebvre.realestatemanager.ui.screen.detailsProperty

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import fr.thomas.lefebvre.realestatemanager.database.Property
import fr.thomas.lefebvre.realestatemanager.database.dao.PropertyDAO
import kotlinx.coroutines.*

class DetailsViewModel(
    private val database: PropertyDAO,
    application: Application,
    private val idProperty: Long
) : AndroidViewModel(application) {


    private val _property = MutableLiveData<Property>()
    val property: LiveData<Property>
        get() = _property


    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)


    private val _address = MutableLiveData<String>()
    val address: LiveData<String>
        get() = _address

    private val _description = MutableLiveData<String>()
    val description: LiveData<String>
        get() = _description

    init {

        initProperty()

    }


    private fun initProperty() {
        uiScope.launch {
            _property.value=loadPropertyFromDatabase()
            _address.value=loadPropertyFromDatabase()!!.address
            _description.value=loadPropertyFromDatabase()!!.description
        }

    }


    private suspend fun loadPropertyFromDatabase(): Property? {
        return withContext(Dispatchers.IO) {
            val propert = database.getProperty(idProperty)

            propert
        }
    }

}




