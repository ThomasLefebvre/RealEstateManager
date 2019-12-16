package fr.thomas.lefebvre.realestatemanager.ui.screen.listProperty


import android.app.AlertDialog
import android.app.DatePickerDialog
import android.icu.text.CaseMap
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.*
import android.widget.DatePicker
import android.widget.Switch
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.NavigationUI.setupActionBarWithNavController
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.textfield.TextInputEditText
import fr.thomas.lefebvre.realestatemanager.R
import fr.thomas.lefebvre.realestatemanager.database.Property
import fr.thomas.lefebvre.realestatemanager.database.PropertyDatabase
import fr.thomas.lefebvre.realestatemanager.database.dao.MediaDAO
import fr.thomas.lefebvre.realestatemanager.database.dao.PropertyDAO
import fr.thomas.lefebvre.realestatemanager.databinding.PropertyFragmentBinding
import fr.thomas.lefebvre.realestatemanager.util.formatDateLongToString
import fr.thomas.lefebvre.realestatemanager.util.initMaxQuery
import fr.thomas.lefebvre.realestatemanager.util.initMinQuery
import kotlinx.android.synthetic.main.activity_edit.*
import kotlinx.android.synthetic.main.property_fragment.*
import kotlinx.android.synthetic.main.query_dialog.view.*
import java.util.*
import kotlin.collections.ArrayList


class PropertyFragment : Fragment(), DatePickerDialog.OnDateSetListener {


    private lateinit var viewModel: PropertyViewModel

    private lateinit var databaseProperty: PropertyDAO
    private lateinit var databaseMedia: MediaDAO

    private lateinit var mAdapter: PropertyAdapter
    private var listPropertyFragment = ArrayList<Property>()

    private var isConvert = false

    private var creationDate: Long = 0//init creation date ton filter dialog
    private var soldDate: Long = 0//init sold date ton filter dialog
    private var dateIsCreationDate: Boolean = true//init boolean for recover type date


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: PropertyFragmentBinding =
            DataBindingUtil.inflate(inflater, R.layout.property_fragment, container, false)



        val application = requireNotNull(this.activity).application


        databaseProperty = PropertyDatabase.getInstance(application).propertyDAO
        databaseMedia = PropertyDatabase.getInstance(application).mediaDAO

        val viewModelFactory =//build view model factory
            PropertyViewModelFactory(
                databaseProperty,
                application
            )

        viewModel = activity!!.run {//build view model
            ViewModelProviders.of(this, viewModelFactory).get(PropertyViewModel::class.java)
        }




        binding.lifecycleOwner = this
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(R.menu.menu_options, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.convertDollarEuro -> {//if click convert icon
                if (viewModel.convertDollarToEuro.value == true) {//if already in euro convert to dollar
                    viewModel.convertToDollar()
                    item.setIcon(R.drawable.ic_attach_money_black_24dp)


                } else {//if already in dollar convert to euro
                    viewModel.convertToEuro()
                    item.setIcon(R.drawable.ic_euro_ic)
                }

            }
            R.id.filterProperty -> {
                alertDialogQuery()//if click icon filter launch alert dialog filter
            }
        }
        return NavigationUI.onNavDestinationSelected(item!!, view!!.findNavController())
                || super.onOptionsItemSelected(item)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        recyclerview_property.layoutManager = layoutManager


        //build recycler view
        recyclerview_property.addItemDecoration(
            DividerItemDecoration(
                requireContext(),
                DividerItemDecoration.VERTICAL
            )
        )
        //set list property when data is ready
        mAdapter =
            PropertyAdapter(
                isConvert,
                databaseMedia,
                listPropertyFragment
            ) { property: Property ->
                articleClick(property)
            }

        recyclerview_property.adapter = mAdapter



        viewModel.convertDollarToEuro.observe(this, Observer { convert ->//observe view model convert to refresh list property
            isConvert = convert
            mAdapter.updateCurrency(isConvert, listPropertyFragment)
            mAdapter.notifyDataSetChanged()

        })

        refreshListProperty()//refresh list property at launch app

        super.onViewCreated(view, savedInstanceState)
    }


    private fun articleClick(property: Property) {//method for remove the item on the click
        val isLarge: Boolean = resources.getBoolean(R.bool.isLarge)
        if (isLarge) {
            viewModel.changeIdProperty(property.idProperty)
        } else {
            viewModel.changeIdProperty(property.idProperty)
            view!!.findNavController().navigate(R.id.action_propertyFragment_to_detailsFragment)
        }
    }

    override fun onResume() {
        viewModel.convertToDollar()
        super.onResume()
    }

    private fun alertDialogQuery() {

        val mDialog = LayoutInflater.from(requireContext())
            .inflate(R.layout.query_dialog, null)//Inflate dialog with custom layout
        val mBuilder = AlertDialog.Builder(requireContext())//build the dialog with custom view
            .setView(mDialog)//custom view (layout)
        val mAlertDialog = mBuilder.show()//show dialog

        onClickSwitch(mDialog.switchSaleSince, true)
        onClickSwitch(mDialog.switchSoldSince, false)

        mDialog.material_text_button_no_filter.setOnClickListener {
            //all list of property
            viewModel.noFilterListProperty()
            refreshListProperty()//refresh list property with no filter
            mAlertDialog.dismiss()//dismiss dialog filter
        }

        mDialog.material_text_button_filter.setOnClickListener {
            //init variable for filter
            val minPrice: Long = initMinQuery(mDialog.input_min_price)//min price if informed or not
            val maxPrice: Long = initMaxQuery(mDialog.input_max_price, Long.MAX_VALUE)//max price if informed or not
            val minRoom: Int = initMinQuery(mDialog.input_min_room).toInt()//min room if informed or not
            val maxRoom: Int = initMaxQuery(mDialog.input_max_room, Int.MAX_VALUE.toLong()).toInt()//max room if informed or not
            val minSurface: Int = initMinQuery(mDialog.input_min_surface).toInt()//min surface if informed or not
            val maxSurface: Int = initMaxQuery(mDialog.input_max_surface, Int.MAX_VALUE.toLong()).toInt()// max surface if informed or not
            val listType = ArrayList<String>()//init list type empty

                if (mDialog.checkBoxHouse.isChecked) {//if checkbox house if checked add house on list type
                    listType.add(getString(R.string.house))
                }
                if (mDialog.checkBoxStudio.isChecked) {//if checkbox studio if checked add house on list type
                    listType.add(getString(R.string.studio))
                }
                if (mDialog.checkBoxApartment.isChecked) {//if checkbox apartment if checked add house on list type
                    listType.add(getString(R.string.apartment))
                }
                if (mDialog.checkBoxVilla.isChecked) {//if checkbox villa if checked add house on list type
                    listType.add(getString(R.string.villa))
                }



            viewModel.filterListProperty(
                mDialog.input_address.text.toString(), //query list of property
                minPrice,
                maxPrice,
                minRoom,
                maxRoom,
                minSurface,
                maxSurface,
                mDialog.switchSoldSince.isChecked,
                mDialog.checkBoxSchool.isChecked,
                mDialog.checkBoxSport.isChecked,
                mDialog.checkBoxTransport.isChecked,
                mDialog.checkBoxParc.isChecked,
                creationDate,
                soldDate,
                listType
            )
            refreshListProperty() //refresh list property with filter
            mAlertDialog.dismiss()//dismiss dialog filter

        }


    }


    private fun onClickSwitch(switch: Switch, typeDate: Boolean) {
        switch.setOnClickListener {
            //switch listener
            if (switch.isChecked) {
                showDatePickerDialog(switch)//show dialog picker date
                dateIsCreationDate = typeDate //type date for get date if creation or sold date

            }
        }
    }

    private fun showDatePickerDialog(switch: Switch) {
        val datePickerDialog = DatePickerDialog(//build datepicker dialog and
            requireContext(),
            this,
            Calendar.getInstance().get(Calendar.YEAR),
            Calendar.getInstance().get(Calendar.MONTH),
            Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.show()//show date picker dialog

        datePickerDialog.setOnCancelListener {
            //if click on cancel button, swtich the button switch to false (no date)
            switch.isChecked = false
        }
    }

    override fun onDateSet(p0: DatePicker?, year: Int, month: Int, day: Int) {
        val calendar = GregorianCalendar(year, month, day)
        val dateSet = calendar.timeInMillis
        Toast.makeText(requireContext(), formatDateLongToString(dateSet), Toast.LENGTH_LONG).show()
        if (dateIsCreationDate) {//if is creation date
            creationDate = dateSet
            Toast.makeText(
                requireContext(),
                "Creation date is :" + formatDateLongToString(dateSet),
                Toast.LENGTH_LONG
            ).show()
        } else {//else if sold date
            Toast.makeText(
                requireContext(),
                "Sold date is :" + formatDateLongToString(dateSet),
                Toast.LENGTH_LONG
            ).show()
            soldDate = dateSet
        }


    }

    private fun refreshListProperty() {
        viewModel.listProperty.observe(this, Observer { propertyFilter ->

            listPropertyFragment.clear()
            listPropertyFragment.addAll(propertyFilter)
            mAdapter.notifyDataSetChanged()

        })
    }


}
