package fr.thomas.lefebvre.realestatemanager.ui.screen.loanCalculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import fr.thomas.lefebvre.realestatemanager.R

class LoanCalculatorActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loan_calculator)
        setTitle(getString(R.string.loan))
    }
}
