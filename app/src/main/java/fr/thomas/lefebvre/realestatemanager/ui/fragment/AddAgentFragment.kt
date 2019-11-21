package fr.thomas.lefebvre.realestatemanager.ui.fragment

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil

import fr.thomas.lefebvre.realestatemanager.R
import fr.thomas.lefebvre.realestatemanager.database.PropertyDatabase
import fr.thomas.lefebvre.realestatemanager.databinding.AddAgentFragmentBinding
import fr.thomas.lefebvre.realestatemanager.databinding.AddPropertyFragmentBinding
import fr.thomas.lefebvre.realestatemanager.ui.viewmodel.AddAgentViewModel
import fr.thomas.lefebvre.realestatemanager.ui.viewmodel.AddAgentViewModelFactory
import fr.thomas.lefebvre.realestatemanager.ui.viewmodel.AddPropertyViewModel
import fr.thomas.lefebvre.realestatemanager.ui.viewmodel.AddViewModelFactory

class AddAgentFragment : Fragment() {

    private lateinit var viewModel:AddAgentViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: AddAgentFragmentBinding =
            DataBindingUtil.inflate(inflater, R.layout.add_agent_fragment, container, false)

        val application = requireNotNull(this.activity).application


        val databaseAgent = PropertyDatabase.getInstance(application).agentDAO

        val viewModelFactory = AddAgentViewModelFactory(databaseAgent, application)

        viewModel =
            ViewModelProviders.of(this, viewModelFactory).get(AddAgentViewModel::class.java)


        binding.addAgentViewModel = viewModel
        binding.lifecycleOwner = this

        return binding.root
    }



}