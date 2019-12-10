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

    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

   internal val listProperty=database.getAllProperty()

    private val _idProperty=MutableLiveData<Long>()

    val idProperty:LiveData<Long>
    get() = _idProperty

    val convertDollarToEuro=MutableLiveData<Boolean>()





    fun changeIdProperty(idProperty:Long){
        _idProperty.value=idProperty
    }

    init {
        initLastId()
        convertDollarToEuro.value=false
    }




    fun initLastId() {
        uiScope.launch {
           _idProperty.value=loadLastIdFromDatabase()

        }

    }


    private suspend fun loadLastIdFromDatabase(): Long? {
        return withContext(Dispatchers.IO) {
            val lastId = database.getLastPropertyID()

            lastId
        }
    }

    fun convertToEuro(){
        convertDollarToEuro.value=true
    }

    fun convertToDollar(){
        convertDollarToEuro.value=false
    }







}
