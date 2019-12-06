package fr.thomas.lefebvre.realestatemanager.ui.screen.listProperty

import android.net.Uri
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.NavDirections
import androidx.navigation.Navigation.findNavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.onNavDestinationSelected
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import fr.thomas.lefebvre.realestatemanager.R
import fr.thomas.lefebvre.realestatemanager.database.Property
import fr.thomas.lefebvre.realestatemanager.database.PropertyDatabase
import fr.thomas.lefebvre.realestatemanager.database.dao.MediaDAO
import fr.thomas.lefebvre.realestatemanager.database.dao.PropertyDAO
import fr.thomas.lefebvre.realestatemanager.databinding.AddAgentFragmentBinding
import fr.thomas.lefebvre.realestatemanager.databinding.PropertyFragmentBinding
import fr.thomas.lefebvre.realestatemanager.ui.screen.addAgent.AddAgentViewModel
import fr.thomas.lefebvre.realestatemanager.ui.screen.addAgent.AddAgentViewModelFactory
import fr.thomas.lefebvre.realestatemanager.ui.screen.addProperty.PhotoAdapter
import kotlinx.android.synthetic.main.activity_add_property.*
import kotlinx.android.synthetic.main.property_fragment.*


class PropertyFragment : Fragment() {


    private lateinit var viewModel: PropertyViewModel

    private lateinit var databaseProperty: PropertyDAO
    private lateinit var databaseMedia: MediaDAO


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: PropertyFragmentBinding =
            DataBindingUtil.inflate(inflater, R.layout.property_fragment, container, false)

        val application = requireNotNull(this.activity).application


        databaseProperty = PropertyDatabase.getInstance(application).propertyDAO
        databaseMedia = PropertyDatabase.getInstance(application).mediaDAO

        val viewModelFactory =
            PropertyViewModelFactory(
                databaseProperty,
                application
            )

        viewModel = activity!!.run {
            ViewModelProviders.of(this, viewModelFactory).get(PropertyViewModel::class.java)
        }

        binding.lifecycleOwner = this

        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(R.menu.menu_options,menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return NavigationUI.onNavDestinationSelected(item!!,view!!.findNavController())
                ||super.onOptionsItemSelected(item)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {


        val layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)


        recyclerview_property.layoutManager = layoutManager
        recyclerview_property.addItemDecoration(
            DividerItemDecoration(
                requireContext(),
                DividerItemDecoration.VERTICAL
            )
        )

        viewModel.listProperty.observe(this, Observer { propertys ->
            recyclerview_property.adapter =
                PropertyAdapter(databaseMedia, propertys) { property: Property ->
                    articleClick(property)
                }
        })


        super.onViewCreated(view, savedInstanceState)
    }


    private fun articleClick(property: Property) {//method for remove the item on the clic
        val isLarge: Boolean = resources.getBoolean(R.bool.isLarge)
        if (isLarge) {
            viewModel.changeIdProperty(property.idProperty)
        } else {
            viewModel.changeIdProperty(property.idProperty)
            view!!.findNavController().navigate(R.id.action_propertyFragment_to_detailsFragment)
        }


    }


}
