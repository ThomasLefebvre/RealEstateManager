package fr.thomas.lefebvre.realestatemanager.ui.screen.detailsProperty

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import fr.thomas.lefebvre.realestatemanager.R
import fr.thomas.lefebvre.realestatemanager.database.Media
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
        val cardView: CardView = itemView.findViewById(R.id.cardViewSold)


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