package fr.thomas.lefebvre.realestatemanager.ui.screen.listProperty

import android.media.Image
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Database
import fr.thomas.lefebvre.realestatemanager.R
import fr.thomas.lefebvre.realestatemanager.database.Media
import fr.thomas.lefebvre.realestatemanager.database.Property
import fr.thomas.lefebvre.realestatemanager.database.PropertyDatabase
import fr.thomas.lefebvre.realestatemanager.database.dao.MediaDAO
import fr.thomas.lefebvre.realestatemanager.ui.screen.addProperty.PhotoAdapter
import kotlinx.coroutines.*
import org.w3c.dom.Text
import androidx.databinding.adapters.TextViewBindingAdapter.setText
import android.os.Looper
import android.os.Handler
import com.squareup.picasso.Picasso
import fr.thomas.lefebvre.realestatemanager.util.formatAddress
import fr.thomas.lefebvre.realestatemanager.util.formatPriceToStringPrice


class PropertyAdapter(
    private val database: MediaDAO,
    private val listProperty: List<Property>,
    private val listener: (Property) -> Unit

) : RecyclerView.Adapter<PropertyAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.rv_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listProperty.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(database, listProperty[position], listener)
    }

    class ViewHolder(item: View) : RecyclerView.ViewHolder(item) {

        val textViewTypeProperty: TextView = itemView.findViewById(R.id.textViewTypeProperty)
        val textViewCityProperty: TextView = itemView.findViewById(R.id.textViewCityProperty)
        val textViewPriceProperty: TextView = itemView.findViewById(R.id.textViewPriceProperty)
        val imageViewProperty: ImageView = itemView.findViewById(R.id.imageViewPropertyList)

        private var viewModelJob = Job()
        private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)


        fun bind(database: MediaDAO, property: Property, listener: (Property) -> Unit) {

            textViewTypeProperty.text = property.type
            val city = formatAddress(property.address)
            textViewCityProperty.text = city
            textViewPriceProperty.text = formatPriceToStringPrice(property.price)

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
                listener(property)
            }
        }
    }


}