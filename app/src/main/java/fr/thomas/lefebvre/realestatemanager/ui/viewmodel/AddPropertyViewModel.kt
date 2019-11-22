package fr.thomas.lefebvre.realestatemanager.ui.viewmodel

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.databinding.Bindable
import androidx.lifecycle.*
import fr.thomas.lefebvre.realestatemanager.R
import fr.thomas.lefebvre.realestatemanager.database.Address
import fr.thomas.lefebvre.realestatemanager.database.Agent
import fr.thomas.lefebvre.realestatemanager.database.Property
import fr.thomas.lefebvre.realestatemanager.database.PropertyDatabase
import fr.thomas.lefebvre.realestatemanager.database.dao.AgentDAO
import fr.thomas.lefebvre.realestatemanager.database.dao.PropertyDAO
import fr.thomas.lefebvre.realestatemanager.util.formatListAgentToListString
import kotlinx.coroutines.*

class AddPropertyViewModel(
    val database: PropertyDAO,
    val databaseAgent: AgentDAO,
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


    init {

        initData()
    }

    fun initData() {
        editTextComplement.value = ""
        listType.value = listOf("House", "Apartment", "Villa", "Studio", "Castle")


    }


    fun onSaveProperty(idAgent: Long, type: String) {

        if (allFieldInformed()) {
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
                    0L,
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
                cleanAllEditText()

            }

        } else {
            Toast.makeText(getApplication(), R.string.complete_all_informations, Toast.LENGTH_LONG).show()


        }

    }

    private suspend fun insertNewProperty(property: Property) {
        withContext(Dispatchers.IO) {
            database.insert(property)
            Log.i("DEBUG", "Insert new property in database ${property.idProperty}")

        }

    }

    private fun cleanAllEditText() {
        editTextPrice.value = null
        editTextSurface.value = null
        editTextRoom.value = null
        editTextPostalCode.value = null
        editTextCity.value = null
        editTextNameWay.value = null
        editTextTypeWay.value = null
        editTextNumWay.value = null
        editTextDescription.value = null
        editTextComplement.value=""
        editTextState.value=null

    }

    private fun allFieldInformed(): Boolean =
        (editTextState.value != null
                && editTextPrice.value != null
                && editTextSurface.value != null
                && editTextRoom.value != null
                && editTextPostalCode.value != null
                && editTextCity.value != null
                && editTextNameWay.value != null
                && editTextTypeWay.value != null
                && editTextNumWay.value != null
                && editTextDescription.value != null)


}
