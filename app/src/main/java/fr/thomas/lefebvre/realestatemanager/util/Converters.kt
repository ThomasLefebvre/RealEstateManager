package fr.thomas.lefebvre.realestatemanager.util

import android.net.Uri
import androidx.room.TypeConverter
import fr.thomas.lefebvre.realestatemanager.database.Agent

class Converters {


    @TypeConverter
    fun uriToString(uri: Uri): String {
        return uri.toString()
    }

    @TypeConverter
    fun stringToUri(string: String): Uri {
        return Uri.parse(string)
    }


}