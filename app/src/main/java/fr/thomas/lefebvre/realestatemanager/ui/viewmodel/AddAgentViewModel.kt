package fr.thomas.lefebvre.realestatemanager.ui.viewmodel

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import fr.thomas.lefebvre.realestatemanager.database.Agent
import fr.thomas.lefebvre.realestatemanager.database.dao.AgentDAO
import fr.thomas.lefebvre.realestatemanager.database.dao.PropertyDAO
import kotlinx.coroutines.*

class AddAgentViewModel (
    val database: AgentDAO,
    application: Application
) : AndroidViewModel(application) {
    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    val editTextNameAgent=MutableLiveData<String>()
    val editTextEmailAgent=MutableLiveData<String>()




    fun onSaveAgent(){

        uiScope.launch {
            if(allFieldInformed()){
                val newAgent=Agent(0L,editTextNameAgent.value!!,editTextEmailAgent.value!!)
                insertNewAgent(newAgent)
                cleanAllEditText()
                Toast.makeText(getApplication(),"Insert agent ${newAgent.name}", Toast.LENGTH_LONG).show()
            }
            else{
                Toast.makeText(getApplication(),"Complete the informations", Toast.LENGTH_LONG).show()
            }

        }
    }

    private suspend fun insertNewAgent(newAgent:Agent){
        withContext(Dispatchers.IO){
            database.insert(newAgent)

        }
    }

    private fun cleanAllEditText() {
        editTextEmailAgent.value = null
        editTextNameAgent.value = null


    }

    private fun allFieldInformed():Boolean=(editTextEmailAgent.value!=null&&
            editTextNameAgent.value!=null)


}
