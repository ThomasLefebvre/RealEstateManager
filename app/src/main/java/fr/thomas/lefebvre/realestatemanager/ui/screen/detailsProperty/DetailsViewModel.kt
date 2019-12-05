package fr.thomas.lefebvre.realestatemanager.ui.screen.detailsProperty

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import fr.thomas.lefebvre.realestatemanager.database.Media
import fr.thomas.lefebvre.realestatemanager.database.Property
import fr.thomas.lefebvre.realestatemanager.database.dao.MediaDAO
import fr.thomas.lefebvre.realestatemanager.database.dao.PropertyDAO
import kotlinx.coroutines.*

class DetailsViewModel(
    private val database: PropertyDAO,
    private val databaseMedia:MediaDAO,
    application: Application

) : AndroidViewModel(application) {


    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val _listMedia=MutableLiveData<List<Media>>()
    val listMedia: LiveData<List<Media>>
        get() = _listMedia


    private val _property = MutableLiveData<Property>()
    val property: LiveData<Property>
        get() = _property




    private val _address = MutableLiveData<String>()
    val address: LiveData<String>
        get() = _address

    private val _description = MutableLiveData<String>()
    val description: LiveData<String>
        get() = _description

    init {



    }


     fun initProperty(idProperty: Long) {
        uiScope.launch {
            _property.value=loadPropertyFromDatabase(idProperty)
            _address.value=property.value?.address
            _description.value=property.value?.description
        }

    }


    private suspend fun loadPropertyFromDatabase(idProperty: Long): Property? {
        return withContext(Dispatchers.IO) {
            val propert = database.getProperty(idProperty)

            propert
        }
    }

    fun initMedia(idProperty: Long) {
        uiScope.launch {
            _listMedia.value=loadMediaFromDatabase(idProperty)
        }

    }


    private suspend fun loadMediaFromDatabase(idProperty: Long): List<Media>? {
        return withContext(Dispatchers.IO) {
            val listMedia = databaseMedia.getListMediaWithIdProperty(idProperty)

            listMedia
        }
    }



}




