package fr.thomas.lefebvre.realestatemanager.ui.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import fr.thomas.lefebvre.realestatemanager.database.dao.AgentDAO
import fr.thomas.lefebvre.realestatemanager.database.dao.PropertyDAO

class AddAgentViewModelFactory (
    private val dataAgent:AgentDAO,
    private val application: Application
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddAgentViewModel::class.java)) {
            return AddAgentViewModel(dataAgent, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}