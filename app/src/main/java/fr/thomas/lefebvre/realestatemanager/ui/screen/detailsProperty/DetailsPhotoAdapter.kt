package fr.thomas.lefebvre.realestatemanager.ui.screen.detailsProperty

import android.media.Image
import android.net.Uri
import android.net.Uri.EMPTY
import android.opengl.Visibility
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
import androidx.cardview.widget.CardView
import com.squareup.picasso.Picasso


class DetailsPhotoAdapter(
    private val listMedia: List<Media>


) : RecyclerView.Adapter<DetailsPhotoAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.rv_details_photo, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listMedia.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(listMedia[position])
    }

    class ViewHolder(item: View) : RecyclerView.ViewHolder(item) {

        val textDescription: TextView = itemView.findViewById(R.id.textViewDescription)
        val photoProperty: ImageView = itemView.findViewById(R.id.imageViewPhotoDetails)
        val cardView: CardView = itemView.findViewById(R.id.cardViewDescription)


        fun bind(media: Media) {

            textDescription.text = media.descriptionPhoto

            Picasso
                .get()
                .load(media.photo)
                .resize(800, 800)
                .centerCrop()
                .into(photoProperty)


            if (media.descriptionPhoto != "") {
                cardView.visibility = View.VISIBLE
            } else {
                cardView.visibility = View.GONE
            }


        }
    }


}