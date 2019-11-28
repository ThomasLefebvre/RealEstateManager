package fr.thomas.lefebvre.realestatemanager.ui.screen.addAgent

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil

import fr.thomas.lefebvre.realestatemanager.R
import fr.thomas.lefebvre.realestatemanager.database.PropertyDatabase
import fr.thomas.lefebvre.realestatemanager.databinding.AddAgentFragmentBinding
import fr.thomas.lefebvre.realestatemanager.util.validateEmail
import kotlinx.android.synthetic.main.add_agent_fragment.*

class AddAgentFragment : Fragment() {

    private lateinit var viewModel: AddAgentViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: AddAgentFragmentBinding =
            DataBindingUtil.inflate(inflater, R.layout.add_agent_fragment, container, false)

        val application = requireNotNull(this.activity).application


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

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        checkEmail()
        super.onViewCreated(view, savedInstanceState)
    }

    fun checkEmail() {
        inputEmail.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                if (validateEmail(viewModel.editTextEmailAgent.value!!)) {
                    textInputLayoutEmail.isErrorEnabled = false
                } else {
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
