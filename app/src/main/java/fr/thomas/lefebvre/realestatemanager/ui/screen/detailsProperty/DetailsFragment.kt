package fr.thomas.lefebvre.realestatemanager.ui.screen.detailsProperty

import android.app.Application
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager

import fr.thomas.lefebvre.realestatemanager.R
import fr.thomas.lefebvre.realestatemanager.database.Property
import fr.thomas.lefebvre.realestatemanager.database.PropertyDatabase
import fr.thomas.lefebvre.realestatemanager.database.dao.MediaDAO
import fr.thomas.lefebvre.realestatemanager.database.dao.PropertyDAO
import fr.thomas.lefebvre.realestatemanager.databinding.DetailsFragmentBinding
import fr.thomas.lefebvre.realestatemanager.databinding.PropertyFragmentBinding
import fr.thomas.lefebvre.realestatemanager.ui.screen.listProperty.PropertyAdapter
import fr.thomas.lefebvre.realestatemanager.ui.screen.listProperty.PropertyViewModel
import fr.thomas.lefebvre.realestatemanager.ui.screen.listProperty.PropertyViewModelFactory
import kotlinx.android.synthetic.main.details_fragment.*
import kotlinx.android.synthetic.main.property_fragment.*

class DetailsFragment : Fragment() {


    private lateinit var viewModel: DetailsViewModel

    private lateinit var databaseProperty: PropertyDAO

    private lateinit var databaseMedia: MediaDAO

    private lateinit var viewModelProperty: PropertyViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: DetailsFragmentBinding =
            DataBindingUtil.inflate(inflater, R.layout.details_fragment, container, false)

        val application = requireNotNull(this.activity).application


        databaseProperty = PropertyDatabase.getInstance(application).propertyDAO
        databaseMedia = PropertyDatabase.getInstance(application).mediaDAO


        //init the property view model
        val viewModelFactoryProperty=PropertyViewModelFactory(databaseProperty,application)
        viewModelProperty=activity!!.run {ViewModelProviders.of(this,viewModelFactoryProperty).get(PropertyViewModel::class.java)  }



        //init the details view model
        val viewModelFactory =
            DetailsViewModelFactory(
                databaseProperty,
                databaseMedia,
                application

            )
        viewModel =
            ViewModelProviders.of(this, viewModelFactory).get(DetailsViewModel::class.java)

        binding.detailsFragmentViewModel = viewModel








        binding.lifecycleOwner = this

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {


            //if tab update the idProperty with observable liveData on the property view model
            viewModelProperty.idProperty.observe(this, Observer { idProperty->
                viewModel.initProperty(idProperty)
                viewModel.initMedia(idProperty)

            })





        setRecyclerViewPhoto()




        super.onViewCreated(view, savedInstanceState)
    }


    fun setRecyclerViewPhoto() {
        val layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)


        recycler_view_photo_details.layoutManager = layoutManager

        viewModel.listMedia.observe(this, Observer { medias ->
            if (medias.isNotEmpty()) {
                recycler_view_photo_details.adapter = DetailsPhotoAdapter(medias)
                recycler_view_photo_details.visibility=View.VISIBLE
                textViewNoPhoto.visibility = View.GONE
            } else {
                textViewNoPhoto.visibility = View.VISIBLE
                recycler_view_photo_details.visibility=View.GONE
            }
        })
    }

    fun initDataForMobile(application: Application, binding: DetailsFragmentBinding) {
        val viewModelFactory =
            DetailsViewModelFactory(
                databaseProperty,
                databaseMedia,
                application



            )
        viewModel =
            ViewModelProviders.of(this, viewModelFactory).get(DetailsViewModel::class.java)

        binding.detailsFragmentViewModel = viewModel
    }


}
