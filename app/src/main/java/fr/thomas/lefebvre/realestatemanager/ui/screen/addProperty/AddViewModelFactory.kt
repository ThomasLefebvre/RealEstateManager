package fr.thomas.lefebvre.realestatemanager.ui.screen.addProperty

import android.app.Application
import android.media.MediaDataSource
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import fr.thomas.lefebvre.realestatemanager.database.dao.AgentDAO
import fr.thomas.lefebvre.realestatemanager.database.dao.MediaDAO
import fr.thomas.lefebvre.realestatemanager.database.dao.PropertyDAO

class AddViewModelFactory (
    private val dataSource: PropertyDAO,
    private val dataAgent:AgentDAO,
    private val dataPhoto: MediaDAO,
    private val application: Application
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddPropertyViewModel::class.java)) {
            return AddPropertyViewModel(
                dataSource,
                dataAgent,
                dataPhoto,
                application
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}