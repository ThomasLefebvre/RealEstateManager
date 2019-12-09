package fr.thomas.lefebvre.realestatemanager.ui.screen.loanCalculator

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import fr.thomas.lefebvre.realestatemanager.database.dao.AgentDAO

class CalculatorViewModelFactory (
    private val application: Application
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CalculatorViewModel::class.java)) {
            return CalculatorViewModel(
                application
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}