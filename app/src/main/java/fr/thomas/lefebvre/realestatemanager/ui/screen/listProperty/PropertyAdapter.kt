package fr.thomas.lefebvre.realestatemanager.ui.screen.listProperty

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import fr.thomas.lefebvre.realestatemanager.R
import fr.thomas.lefebvre.realestatemanager.database.Property
import fr.thomas.lefebvre.realestatemanager.database.dao.MediaDAO
import kotlinx.coroutines.*
import android.os.Looper
import android.os.Handler
import androidx.cardview.widget.CardView
import com.squareup.picasso.Picasso
import fr.thomas.lefebvre.realestatemanager.util.*


class PropertyAdapter(
    private var convertToEuro: Boolean,
    private val database: MediaDAO,
    private val listProperty: ArrayList<Property>,
    private val listener: (Int, Property) -> Unit

) : RecyclerView.Adapter<PropertyAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.rv_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listProperty.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(convertToEuro, database, listProperty[position], listener)
    }

    fun updateCurrency(newConvertToEuro: Boolean, listProperty: ArrayList<Property>) {
        convertToEuro = newConvertToEuro

        listProperty.forEach { property ->
            notifyItemChanged(listProperty.indexOf(property))
        }

    }


    class ViewHolder(item: View) : RecyclerView.ViewHolder(item) {

        val textViewTypeProperty: TextView = itemView.findViewById(R.id.textViewTypeProperty)
        val textViewCityProperty: TextView = itemView.findViewById(R.id.textViewCityProperty)
        val textViewPriceProperty: TextView = itemView.findViewById(R.id.textViewPriceProperty)
        val imageViewProperty: ImageView = itemView.findViewById(R.id.imageViewPropertyList)
        val cardViewSold: CardView = itemView.findViewById(R.id.cardViewSold)

        private var viewModelJob = Job()
        private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)


        fun bind(
            convertToEuro: Boolean,
            database: MediaDAO,
            property: Property,
            listener: (Int, Property) -> Unit
        ) {

            textViewTypeProperty.text = property.type
            val city = formatAddress(property.address)
            textViewCityProperty.text = city


            if (property.price != null) {
                if (convertToEuro) {
                    textViewPriceProperty.text =
                        formatPriceToStringPriceEuro(convertDollarToEuro(property.price!!))
                } else {
                    textViewPriceProperty.text = formatPriceToStringPriceDollar(property.price)
                }
            } else {
                textViewPriceProperty.text = formatPriceToStringPriceDollar(property.price)
            }



            if (property.stateProperty) {
                cardViewSold.visibility = View.VISIBLE
            } else {
                cardViewSold.visibility = View.GONE
            }

            uiScope.launch {

                withContext(Dispatchers.IO) {
                    val listUri = database.getUriPhoto(property.idProperty)
                    if (listUri!!.isNotEmpty()) {

                        val uri = listUri[0]

                        Handler(Looper.getMainLooper()).post(Runnable {

                            Picasso
                                .get()
                                .load(uri)
                                .resize(300, 300)
                                .centerCrop()
                                .into(imageViewProperty)

                        })

                    } else {

                        Handler(Looper.getMainLooper()).post(Runnable {

                            Picasso
                                .get()
                                .load(R.drawable.house)
                                .resize(500, 500)
                                .centerCrop()
                                .into(imageViewProperty)

                        })
                    }
                }

            }
            itemView.setOnClickListener {
                listener(adapterPosition, property)
            }
        }
    }


}