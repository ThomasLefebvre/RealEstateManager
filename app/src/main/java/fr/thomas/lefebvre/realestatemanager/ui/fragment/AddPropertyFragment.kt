package fr.thomas.lefebvre.realestatemanager.ui.fragment

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import fr.thomas.lefebvre.realestatemanager.R
import fr.thomas.lefebvre.realestatemanager.ui.viewmodel.AddPropertyViewModel

class AddPropertyFragment : Fragment() {

    companion object {
        fun newInstance() = AddPropertyFragment()
    }

    private lateinit var viewModel: AddPropertyViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.add_property_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(AddPropertyViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
