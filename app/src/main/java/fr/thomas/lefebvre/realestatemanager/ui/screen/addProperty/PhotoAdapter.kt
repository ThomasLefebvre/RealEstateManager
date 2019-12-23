package fr.thomas.lefebvre.realestatemanager.ui.screen.addProperty

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import fr.thomas.lefebvre.realestatemanager.R


class PhotoAdapter(
    private val listUriPhoto: ArrayList<Uri>,
    private val listener: (Int, Uri) -> Unit
) : RecyclerView.Adapter<PhotoAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.rv_photo_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listUriPhoto.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position, listUriPhoto[position], listener)
    }

    class ViewHolder(item: View) : RecyclerView.ViewHolder(item) {
        val photo: ImageView = itemView.findViewById(R.id.imageViewPhotoAddProperty)
        val iconSuppres: ImageView = itemView.findViewById(R.id.iconSuppress)
        val descriptionPhoto: EditText = itemView.findViewById(R.id.editTextDescriptionRv)

        fun bind(position: Int, uri: Uri, listener: (Int, Uri) -> Unit) {
            Picasso//display photo
                .get()
                .load(uri)
                .resize(300,300)
                .centerCrop()
                .into(photo)


            descriptionPhoto.text = null//set up init text


            iconSuppres.setOnClickListener {
                listener(adapterPosition, uri)
            }
        }
    }


}
