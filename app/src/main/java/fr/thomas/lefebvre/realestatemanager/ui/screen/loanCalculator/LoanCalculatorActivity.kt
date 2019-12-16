package fr.thomas.lefebvre.realestatemanager.ui.screen.loanCalculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import fr.thomas.lefebvre.realestatemanager.R
import fr.thomas.lefebvre.realestatemanager.databinding.ActivityLoanCalculatorBinding

class LoanCalculatorActivity : AppCompatActivity() {

    private lateinit var viewModel: CalculatorViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityLoanCalculatorBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_loan_calculator)
        setTitle(getString(R.string.loan))

        val application = requireNotNull(this.application)
        val viewModelFactory = CalculatorViewModelFactory(application)
        viewModel =
            ViewModelProviders.of(this, viewModelFactory).get(CalculatorViewModel::class.java)

        binding.viewModelCalculator = viewModel
        binding.lifecycleOwner = this
        setTitle(getString(R.string.loan))
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }


    override fun onSupportNavigateUp(): Boolean {//set action with click on support action bar
        onBackPressed()
        return true
    }
}
