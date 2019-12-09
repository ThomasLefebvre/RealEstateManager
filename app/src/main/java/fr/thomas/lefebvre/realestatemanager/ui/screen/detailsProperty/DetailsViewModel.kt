package fr.thomas.lefebvre.realestatemanager.ui.screen.detailsProperty

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.maps.model.LatLng
import fr.thomas.lefebvre.realestatemanager.database.Media
import fr.thomas.lefebvre.realestatemanager.database.Property
import fr.thomas.lefebvre.realestatemanager.database.dao.MediaDAO
import fr.thomas.lefebvre.realestatemanager.database.dao.PropertyDAO
import fr.thomas.lefebvre.realestatemanager.util.*
import kotlinx.coroutines.*

class DetailsViewModel(
    private val database: PropertyDAO,
    private val databaseMedia: MediaDAO,
    application: Application

) : AndroidViewModel(application) {


    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val _listMedia = MutableLiveData<List<Media>>()
    val listMedia: LiveData<List<Media>>
        get() = _listMedia


    private val _property = MutableLiveData<Property>()
    val property: LiveData<Property>
        get() = _property


    private val _type = MutableLiveData<String>()
    val type: LiveData<String>
        get() = _type

     val _surface = MutableLiveData<String>()
    val surface: LiveData<String>
        get() = _surface

     val _room = MutableLiveData<String>()
    val room: LiveData<String>
        get() = _room

     val _price = MutableLiveData<String>()
    val price: LiveData<String>
        get() = _price

    private val _agent = MutableLiveData<String>()
    val agent: LiveData<String>
        get() = _agent

     val _address = MutableLiveData<String>()
    val address: LiveData<String>
        get() = _address

     val _description = MutableLiveData<String>()
    val description: LiveData<String>
        get() = _description

     val _sports = MutableLiveData<Boolean>()
    val sports: LiveData<Boolean>
        get() = _sports

     val _school = MutableLiveData<Boolean>()
    val school: LiveData<Boolean>
        get() = _school


     val _transport = MutableLiveData<Boolean>()
    val transport: LiveData<Boolean>
        get() = _transport

     val _parc = MutableLiveData<Boolean>()
    val parc: LiveData<Boolean>
        get() = _parc

    private val _lat = MutableLiveData<Double>()
    val lat: LiveData<Double>
        get() = _lat

    private val _lng = MutableLiveData<Double>()
    val lng: LiveData<Double>
        get() = _lng

    private val _latLng = MutableLiveData<LatLng>()
    val latLng: LiveData<LatLng>
        get() = _latLng

    private val _locationIsInformed = MutableLiveData<Boolean>()
    val locationIsInformed: LiveData<Boolean>
        get() = _locationIsInformed

    private val latLngDefault = LatLng(-1.0, -1.0)

    private val _dateCreation = MutableLiveData<String>()
    val dateCreation: LiveData<String>
        get() = _dateCreation

   private val _OneIsCreated=MutableLiveData<Boolean>()
    val OnIsCreated:LiveData<Boolean>
    get() = _OneIsCreated

    val stateProperty=MutableLiveData<Boolean>()

    val saleDate=MutableLiveData<Long>()
    val idAgent=MutableLiveData<Long>()



    // ----                            LOAD DETAILS PROPERTY                ----

    fun initPropertyDetails(idProperty: Long) {
        uiScope.launch {

            _property.value = loadPropertyFromDatabase(idProperty)

            if(_property.value!=null){
                initInformationProperty()
                initNearbyPoint()
                initLocationProperty()
                _OneIsCreated.value=true

            }
            else _OneIsCreated.value=false

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
            _listMedia.value = loadMediaFromDatabase(idProperty)
        }

    }


    private suspend fun loadMediaFromDatabase(idProperty: Long): List<Media>? {
        return withContext(Dispatchers.IO) {
            val listMedia = databaseMedia.getListMediaWithIdProperty(idProperty)

            listMedia
        }
    }

    private fun initInformationProperty() {

        _address.value = property.value?.address
        _description.value = formatStringDescription(property.value?.description)
        _type.value = property.value?.type
        _surface.value = formatSurfaceToStringSurface(property.value?.surface)
        _room.value = formatNumberRoomToString(property.value?.numberRoom)
        _price.value = formatPriceToStringPrice(property.value?.price)
        _dateCreation.value= formatDateLongToString(property.value?.creationDate)
        stateProperty.value=property.value!!.stateProperty
        saleDate.value=property.value!!.saleDate
        idAgent.value=property.value!!.idAgent
    }

    private fun initNearbyPoint() {

        _sports.value = property.value?.sport//get the boolean for sport
        _school.value = property.value?.school//get the boolean for school
        _transport.value = property.value?.transport//get the boolean for transport
        _parc.value = property.value?.parc//get the boolean for parc
    }

    private fun initLocationProperty() {

        _lat.value = property.value?.lat//get the lat
        _lng.value = property.value?.lng//get the lng
        _latLng.value = LatLng(_lat.value!!, _lng.value!!)//set the latLng location
        _locationIsInformed.value = _latLng.value != latLngDefault//check if location is informed
    }



    // ---                                 UPDATE  PROPERTY                         ---



    fun initPropertyEdit(idProperty: Long) {
        uiScope.launch {

            _property.value = loadPropertyFromDatabase(idProperty)


            initInformationPropertyEdit()
            initNearbyPointEdit()
            initLocationPropertyEdit()

        }

    }


    private fun initInformationPropertyEdit() {

        _address.value = property.value?.address
        _description.value = property.value?.description
        _type.value = property.value?.type
        _surface.value = property.value?.surface.toString()
        _room.value = property.value?.numberRoom.toString()
        _price.value = property.value?.price.toString()
        stateProperty.value=property.value!!.stateProperty
        saleDate.value=property.value!!.saleDate
        idAgent.value=property.value!!.idAgent
    }

    private fun initNearbyPointEdit() {

        _sports.value = property.value?.sport//get the boolean for sport
        _school.value = property.value?.school//get the boolean for school
        _transport.value = property.value?.transport//get the boolean for transport
        _parc.value = property.value?.parc//get the boolean for parc
    }

    private fun initLocationPropertyEdit() {

        _lat.value = property.value?.lat//get the lat
        _lng.value = property.value?.lng//get the lng
        _latLng.value = LatLng(_lat.value!!, _lng.value!!)//set the latLng location
        _locationIsInformed.value = _latLng.value != latLngDefault//check if location is informed
    }

     fun updateLatLng(lat:Double,lng:Double){
        _lat.value=lat
        _lng.value=lng
    }

    fun updateProperty(idProperty: Long){
        val property=Property(idProperty,type.value,price.value!!.toLong(),surface.value!!.toInt()
        ,room.value!!.toInt(),description.value,address.value,lat.value,lng.value,parc.value!!,
            sports.value!!,school.value!!,transport.value!!,stateProperty.value!!,
          _property.value!!.creationDate,saleDate.value!!,idAgent.value!!)

        uiScope.launch {
            updatePropertyDatabase(property)
        }
    }

    private suspend fun  updatePropertyDatabase(property: Property){
        withContext(Dispatchers.IO){
            database.update(property)
        }
    }


}




