package fr.thomas.lefebvre.realestatemanager.ui.screen.detailsProperty

import android.app.Application
import android.media.MediaDataSource
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import fr.thomas.lefebvre.realestatemanager.database.dao.AgentDAO
import fr.thomas.lefebvre.realestatemanager.database.dao.MediaDAO
import fr.thomas.lefebvre.realestatemanager.database.dao.PropertyDAO

class DetailsViewModelFactory (
    private val dataSource: PropertyDAO,
    private val application: Application,
   private val idProperty:Long

) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DetailsViewModel::class.java)) {
            return DetailsViewModel(
                dataSource,
                application,
                idProperty

            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}