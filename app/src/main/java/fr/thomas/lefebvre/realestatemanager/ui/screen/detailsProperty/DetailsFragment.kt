package fr.thomas.lefebvre.realestatemanager.ui.screen.detailsProperty

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil

import fr.thomas.lefebvre.realestatemanager.R
import fr.thomas.lefebvre.realestatemanager.database.PropertyDatabase
import fr.thomas.lefebvre.realestatemanager.database.dao.MediaDAO
import fr.thomas.lefebvre.realestatemanager.database.dao.PropertyDAO
import fr.thomas.lefebvre.realestatemanager.databinding.DetailsFragmentBinding
import fr.thomas.lefebvre.realestatemanager.databinding.PropertyFragmentBinding
import fr.thomas.lefebvre.realestatemanager.ui.screen.listProperty.PropertyViewModel
import fr.thomas.lefebvre.realestatemanager.ui.screen.listProperty.PropertyViewModelFactory
import kotlinx.android.synthetic.main.details_fragment.*

class DetailsFragment : Fragment() {




    private lateinit var viewModel: DetailsViewModel

    private lateinit var databaseProperty: PropertyDAO



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: DetailsFragmentBinding =
            DataBindingUtil.inflate(inflater, R.layout.details_fragment, container, false)

        val application = requireNotNull(this.activity).application


        databaseProperty = PropertyDatabase.getInstance(application).propertyDAO


        val viewModelFactory =
            DetailsViewModelFactory(
                databaseProperty,
                application,arguments!!.getLong("idProperty")

            )

        viewModel =
            ViewModelProviders.of(this, viewModelFactory).get(DetailsViewModel::class.java)

        binding.detailsFragmentViewModel=viewModel

        binding.lifecycleOwner = this

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {



        button2.setOnClickListener {
            Toast.makeText(requireContext(),viewModel.address.value,Toast.LENGTH_LONG).show()
        }
        super.onViewCreated(view, savedInstanceState)
    }



}
