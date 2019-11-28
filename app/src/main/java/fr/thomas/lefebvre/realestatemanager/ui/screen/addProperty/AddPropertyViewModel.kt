package fr.thomas.lefebvre.realestatemanager.ui.screen.addProperty

import android.app.Application
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.*
import fr.thomas.lefebvre.realestatemanager.R
import fr.thomas.lefebvre.realestatemanager.database.Address
import fr.thomas.lefebvre.realestatemanager.database.Media
import fr.thomas.lefebvre.realestatemanager.database.Property
import fr.thomas.lefebvre.realestatemanager.database.dao.AgentDAO
import fr.thomas.lefebvre.realestatemanager.database.dao.MediaDAO
import fr.thomas.lefebvre.realestatemanager.database.dao.PropertyDAO
import fr.thomas.lefebvre.realestatemanager.util.formatListAgentToListString
import kotlinx.coroutines.*

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
    val editTextPostalCode = MutableLiveData<String>()
    val editTextCity = MutableLiveData<String>()
    val editTextNameWay = MutableLiveData<String>()
    val editTextTypeWay = MutableLiveData<String>()
    val editTextNumWay = MutableLiveData<String>()
    val editTextDescription = MutableLiveData<String>()
    val editTextComplement = MutableLiveData<String>()
    val editTextState = MutableLiveData<String>()

    val listType = MutableLiveData<List<String>>()

    val listAgent = databaseAgent.getAllAgent()

    val listAgentString = Transformations.map(listAgent) { agents ->
        formatListAgentToListString(listAgent.value!!)
    }
    val listUriPhoto = ArrayList<Uri>()
    val listUriPhotoLiveData = MutableLiveData<ArrayList<Uri>>()


    init {

        initData()
    }

    fun initData() {
        editTextComplement.value = ""
        listType.value = listOf("House", "Apartment", "Villa", "Studio", "Castle")


    }


    fun onSaveProperty(idAgent: Long, type: String) {


        uiScope.launch {
            val address = Address(
                editTextCity.value!!,
                editTextPostalCode.value!!.toInt(),
                editTextTypeWay.value!!,
                editTextNameWay.value!!,
                editTextNumWay.value!!.toInt(),
                editTextComplement.value!!,//TODO
                editTextState.value!!
            )
            val property = Property(
                (System.currentTimeMillis() + editTextPostalCode.value!!.toLong()),
                type,
                editTextPrice.value!!.toFloat(),
                editTextRoom.value!!.toInt(),
                editTextDescription.value!!,
                address,
                false,
                System.currentTimeMillis(),
                0, idAgent
            )
            insertNewProperty(property)
            Toast.makeText(
                getApplication(),
                "The property of ${property.address!!.city} is created",
                Toast.LENGTH_LONG
            ).show()

            if (listUriPhoto.isNotEmpty()) {
                onSavePhoto(listUriPhoto, property.idProperty)
            }

        }


    }

    private suspend fun insertNewProperty(property: Property) {
        withContext(Dispatchers.IO) {
            database.insert(property)
            Log.i("DEBUG", "Insert new property in database ${property.idProperty}")

        }

    }

    private fun onSavePhoto(listUri: List<Uri>, idProperty: Long) {
        uiScope.launch {
            listUri.forEach {
                insertMediaOnDataBase(it, idProperty)
            }

        }

    }

    private suspend fun insertMediaOnDataBase(uri: Uri, idProperty: Long) {
        withContext(Dispatchers.IO) {
            val media = Media(0L, uri, idProperty)
            databasePhoto.insert(media)
            Log.i("DEBUG", "MEDIA INSERT ON DATABASE")
        }

    }


    fun addPhotoUriToList(uri: Uri) {
        listUriPhoto.add(uri)
    }
}
