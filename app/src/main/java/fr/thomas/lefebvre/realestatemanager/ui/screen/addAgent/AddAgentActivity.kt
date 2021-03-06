package fr.thomas.lefebvre.realestatemanager.ui.screen.addAgent

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import fr.thomas.lefebvre.realestatemanager.R
import fr.thomas.lefebvre.realestatemanager.database.PropertyDatabase
import fr.thomas.lefebvre.realestatemanager.databinding.ActivityAddAgentBinding
import fr.thomas.lefebvre.realestatemanager.util.validateEmail
import kotlinx.android.synthetic.main.activity_add_agent.*


class AddAgentActivity : AppCompatActivity() {

    private lateinit var viewModel: AddAgentViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityAddAgentBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_add_agent)


        val application = requireNotNull(this).application


        val databaseAgent = PropertyDatabase.getInstance(application).agentDAO

        val viewModelFactory =
            AddAgentViewModelFactory(
                databaseAgent,
                application
            )

        viewModel =
            ViewModelProviders.of(this, viewModelFactory).get(AddAgentViewModel::class.java)


        binding.addAgentViewModel = viewModel
        binding.lifecycleOwner = this

        checkEmail()

        setTitle(getString(R.string.add_new_agent))
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)


    }

    override fun onSupportNavigateUp(): Boolean {//set action with click on support action bar
        onBackPressed()
        return true
    }


    fun checkEmail() {
        inputEmail.addTextChangedListener(object : TextWatcher {
            //text watcher for address mail of agent
            override fun afterTextChanged(p0: Editable?) {
                if (validateEmail(viewModel.editTextEmailAgent.value!!)) {//if mail is ok
                    textInputLayoutEmail.isErrorEnabled = false
                } else {//if email is false
                    textInputLayoutEmail.isErrorEnabled
                    textInputLayoutEmail.error = getString(R.string.email_not_valid)
                }

            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

        })


    }
}
