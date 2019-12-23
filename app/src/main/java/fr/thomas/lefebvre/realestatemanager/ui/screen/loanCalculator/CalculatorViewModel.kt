package fr.thomas.lefebvre.realestatemanager.ui.screen.loanCalculator

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import fr.thomas.lefebvre.realestatemanager.util.calculLoan


class CalculatorViewModel(
    application: Application
) : AndroidViewModel(application) {


    val loanAmount = MutableLiveData<String>()
    val loanTerm = MutableLiveData<String>()
    val loanBring = MutableLiveData<String>()
    val loanInterest = MutableLiveData<String>()

    private val _mensuality = MutableLiveData<String>()
    val mensuality: LiveData<String>
        get() = _mensuality

    private val _totalCost = MutableLiveData<String>()
    val totalCost: LiveData<String>
        get() = _totalCost

    private val _totalInterest = MutableLiveData<String>()
    val totalInterest: LiveData<String>
        get() = _totalInterest


    fun onClickCalcul() {
        if (checkCompleteInfo()) {
            calcul()
        } else {
            Toast.makeText(
                getApplication(),
                "Complete all informations",
                Toast.LENGTH_LONG
            ).show()
        }
    }


    fun calcul() {
        //init variables for the calcul
        val bring: Double
        if (loanBring.value != null && loanBring.value != "") {
            bring = loanBring.value!!.toDouble()
        } else {
            bring = 0.0
        }
        val listCalcul = calculLoan(
            loanAmount.value!!.toDouble(),
            loanTerm.value!!.toDouble(),
            bring,
            loanInterest.value!!.toDouble()
        )

        //set value of mensuality, total cost and total interest
        _mensuality.value = listCalcul[0]
        _totalCost.value = listCalcul[1]
        _totalInterest.value = listCalcul[2]


    }

    fun checkCompleteInfo(): Boolean {
        return loanAmount.value != null && loanInterest.value != null && loanTerm.value != null && loanAmount.value != "" && loanTerm.value != "" && loanInterest.value != ""
    }

}
