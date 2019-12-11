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

     var _listProperty =
        database.getAllPropertyLiveData()//load list property to display on list fragment

//    private val _listProperty = MutableLiveData<List<Property>>()
    val listProperty: LiveData<List<Property>>
        get() = _listProperty


        private val _listPropertyFilter = MutableLiveData<List<Property>>()
    val listPropertyFilter: LiveData<List<Property>>
        get() = _listPropertyFilter

    private val _idProperty = MutableLiveData<Long>()
    val idProperty: LiveData<Long>
        get() = _idProperty

    private val _convertDollarToEuro = MutableLiveData<Boolean>()
    val convertDollarToEuro:LiveData<Boolean>
    get() = _convertDollarToEuro


    fun changeIdProperty(idProperty: Long) {//change id property for details fragment
        _idProperty.value = idProperty
    }

    init {
//        initListProperty()
        initLastId()
        _convertDollarToEuro.value = false
    }

//    private fun initListProperty() {
//        uiScope.launch {
//            _listProperty.value=loadLastListProperty()
//        }
//    }
//
//
//    private suspend fun loadLastListProperty(): List<Property>? {//load last property from database
//        return withContext(Dispatchers.IO) {
//            val listProperty = database.getAllProperty()
//
//            listProperty
//        }
//    }


    fun initLastId() { //init last property created for display on details fragment in tablet at launch
        uiScope.launch {
            _idProperty.value = loadLastIdFromDatabase()

        }

    }

    private suspend fun loadLastIdFromDatabase(): Long? {//load last property from database
        return withContext(Dispatchers.IO) {
            val lastId = database.getLastPropertyID()

            lastId
        }
    }

    fun filterListProperty() {
        uiScope.launch {
            _listPropertyFilter.value = loadListFilterProperty()
        }

    }

    private suspend fun loadListFilterProperty(): List<Property>? {//load last property from database
        return withContext(Dispatchers.IO) {

            val listType= listOf<String>("House")
            val listProperty = database.getAllPropertyQuery("%%",50000,130000,0,10,0,120,false,0,0,true,true,true,true,listType)

            listProperty
        }
    }



    fun convertToEuro() {//change boolean convert
        _convertDollarToEuro.value = true
    }

    fun convertToDollar() {//change boolean convert
        _convertDollarToEuro.value = false
    }


}
