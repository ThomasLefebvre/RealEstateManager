package fr.thomas.lefebvre.realestatemanager.ui.screen.addProperty

import android.app.Application
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.*
import com.google.android.gms.maps.model.LatLng
import fr.thomas.lefebvre.realestatemanager.database.Media
import fr.thomas.lefebvre.realestatemanager.database.Property
import fr.thomas.lefebvre.realestatemanager.database.Type
import fr.thomas.lefebvre.realestatemanager.database.dao.AgentDAO
import fr.thomas.lefebvre.realestatemanager.database.dao.MediaDAO
import fr.thomas.lefebvre.realestatemanager.database.dao.PropertyDAO
import fr.thomas.lefebvre.realestatemanager.util.formatListAgentToListString
import kotlinx.coroutines.*
import kotlin.math.ln

class AddPropertyViewModel(
    val database: PropertyDAO,
    databaseAgent: AgentDAO,
    val databasePhoto: MediaDAO,
    application: Application
) : AndroidViewModel(application) {


    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)


    val editTextPrice = MutableLiveData<String>()
    val editTextSurface = MutableLiveData<String>()
    val editTextRoom = MutableLiveData<String>()
    val editTextDescription = MutableLiveData<String>()
    val editTextComplement = MutableLiveData<String>()

    val editTextAddress = MutableLiveData<String>()
    val lat = MutableLiveData<Double>()
    val lng = MutableLiveData<Double>()

    val parcIsNearby=MutableLiveData<Boolean>()
    val sportIsNearby=MutableLiveData<Boolean>()
    val schoolIsNearby=MutableLiveData<Boolean>()
    val transportIsNearby=MutableLiveData<Boolean>()


    val listType = MutableLiveData<List<String>>()

    val listAgent = databaseAgent.getAllAgent()

    val listAgentString = Transformations.map(listAgent) { agents ->
        formatListAgentToListString(listAgent.value!!)
    }
    val listUriPhoto = ArrayList<Uri>()


    init {

        initData()
    }

    fun initData() {
        editTextComplement.value = ""
        listType.value = listOf(Type.HOUSE.name,Type.APARTMENT.name,Type.STUDIO.name,Type.VILLA.name)
        lat.value=-1.0
        lng.value=-1.0
        parcIsNearby.value=false
        schoolIsNearby.value=false
        sportIsNearby.value=false
        transportIsNearby.value=false

    }


    fun onSaveProperty(idAgent: Long, type: String,listDescriptionPhoto:ArrayList<String>) {

        uiScope.launch {

            val property = Property(
                (System.currentTimeMillis()),
                type,

                editTextPrice.value?.toLong(),
                editTextSurface.value?.toInt(),
                editTextRoom.value?.toInt(),
                editTextDescription.value,
                editTextAddress.value,
                lat.value,
                lng.value,
                parcIsNearby.value!!,
                sportIsNearby.value!!,
                schoolIsNearby.value!!,
                transportIsNearby.value!!,

                false,
                System.currentTimeMillis(),
                0, idAgent
            )
            insertNewProperty(property)
            Toast.makeText(
                getApplication(),
                "The property is created",
                Toast.LENGTH_LONG
            ).show()

            if (listUriPhoto.isNotEmpty()) {
                onSavePhoto(listUriPhoto, property.idProperty,listDescriptionPhoto)
            }
            if (parcIsNearby.value==true){

            }

        }


    }

    private suspend fun insertNewProperty(property: Property) {
        withContext(Dispatchers.IO) {
            database.insert(property)

        }

    }

    private fun onSavePhoto(listUri: List<Uri>, idProperty: Long,listDescriptionPhoto: ArrayList<String>) {
        uiScope.launch {
            listUri.forEachIndexed {index,it->
                insertMediaOnDataBase(it, idProperty,listDescriptionPhoto[index])
            }

        }

    }

    private suspend fun insertMediaOnDataBase(uri: Uri, idProperty: Long,description:String) {
        withContext(Dispatchers.IO) {
            val media = Media(0L, uri, description,idProperty)
            databasePhoto.insert(media)

        }

    }


    fun addPhotoUriToList(uri: Uri) {
        listUriPhoto.add(0,uri)
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }


}
