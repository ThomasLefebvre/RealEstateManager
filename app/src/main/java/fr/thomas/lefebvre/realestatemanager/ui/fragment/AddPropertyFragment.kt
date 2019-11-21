package fr.thomas.lefebvre.realestatemanager.ui.fragment

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil

import fr.thomas.lefebvre.realestatemanager.R
import fr.thomas.lefebvre.realestatemanager.database.PropertyDatabase
import fr.thomas.lefebvre.realestatemanager.databinding.AddPropertyFragmentBinding
import fr.thomas.lefebvre.realestatemanager.ui.viewmodel.AddPropertyViewModel
import fr.thomas.lefebvre.realestatemanager.ui.viewmodel.AddViewModelFactory
import fr.thomas.lefebvre.realestatemanager.ui.viewmodel.PropertyViewModel
import kotlinx.android.synthetic.main.add_property_fragment.*

class AddPropertyFragment : Fragment() {


    lateinit var viewModel: AddPropertyViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: AddPropertyFragmentBinding =
            DataBindingUtil.inflate(inflater, R.layout.add_property_fragment, container, false)

        val application = requireNotNull(this.activity).application

        val database = PropertyDatabase.getInstance(application).propertyDAO
        val databaseAgent = PropertyDatabase.getInstance(application).agentDAO

        val viewModelFactory = AddViewModelFactory(database, databaseAgent, application)

        viewModel =
            ViewModelProviders.of(this, viewModelFactory).get(AddPropertyViewModel::class.java)


        binding.addPropertyViewModel = viewModel
        binding.lifecycleOwner = this

        return binding.root

    }

    fun onClickButtonSave() {
        floatingActionButton.setOnClickListener {
            if (viewModel.listAgent.value != null) {
                val agentId = viewModel.listAgent.value!![spinnerAgent.selectedItemPosition].idAgent
                val type = viewModel.listType.value!![spinnerType.selectedItemPosition]
                viewModel.onSaveProperty(agentId, type)
            } else Toast.makeText(
                requireContext(),
                "Create agent for create a propertie",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        onClickButtonSave()
        super.onViewCreated(view, savedInstanceState)
    }


}
