package fr.thomas.lefebvre.realestatemanager.ui.screen.listProperty

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import fr.thomas.lefebvre.realestatemanager.database.Property
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

    var _listProperty: LiveData<List<Property>> //load list property to display on list fragment

    //    private val _listProperty = MutableLiveData<List<Property>>()
    val listProperty: LiveData<List<Property>>
        get() = _listProperty


    private val _idProperty = MutableLiveData<Long>()
    val idProperty: LiveData<Long>
        get() = _idProperty

    private val _convertDollarToEuro = MutableLiveData<Boolean>()
    val convertDollarToEuro: LiveData<Boolean>
        get() = _convertDollarToEuro


    init {


        _listProperty = database.getAllPropertyLiveData()//        initListProperty()


        Log.d("DEBUG", "init view model property")
        initLastId()
        _convertDollarToEuro.value = false
    }

    fun changeIdProperty(idProperty: Long) {//change id property for details fragment
        _idProperty.value = idProperty
    }


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

    fun filterListProperty(
        address: String,
        minPrice: Long,
        maxPrice: Long,
        minRoom: Int,
        maxRoom: Int,
        minSurface: Int,
        maxSurface: Int,
        sold: Boolean,
        school: Boolean,
        sport: Boolean,
        transport: Boolean,
        parc: Boolean,
        creationDate: Long,
        soldDate: Long,
        listType: List<String>
    ) {

        _listProperty = database.getAllPropertyQuery(
            "%$address%",
            minPrice,
            maxPrice,
            minRoom,
            maxRoom,
            minSurface,
            maxSurface,
            sold,
            creationDate,
            soldDate,
            school,
            sport,
            transport,
            parc,
            listType
        )
    }

    fun noFilterListProperty() {
        _listProperty = database.getAllPropertyLiveData()
    }


    fun convertToEuro() {//change boolean convert
        _convertDollarToEuro.value = true
    }

    fun convertToDollar() {//change boolean convert
        _convertDollarToEuro.value = false
    }


    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }


}
