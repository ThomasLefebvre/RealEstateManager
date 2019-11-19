package fr.thomas.lefebvre.realestatemanager.ui.viewmodel

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import fr.thomas.lefebvre.realestatemanager.database.Property
import fr.thomas.lefebvre.realestatemanager.database.PropertyDatabase
import fr.thomas.lefebvre.realestatemanager.database.dao.PropertyDAO
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class AddPropertyViewModel(val database:PropertyDAO) : ViewModel() {

    private var viewModelJob= Job()
    private val uiScope= CoroutineScope(Dispatchers.Main+viewModelJob)



    fun onSaveProperty(){
        uiScope.launch {
            val property=Property()
        }
    }
    private suspend fun insertNewProperty(property: Property){
        database.insert(property)
    }

}
