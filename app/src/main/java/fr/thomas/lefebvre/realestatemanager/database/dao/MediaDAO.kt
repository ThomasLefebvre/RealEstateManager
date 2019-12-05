package fr.thomas.lefebvre.realestatemanager.database.dao

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import fr.thomas.lefebvre.realestatemanager.database.Media

@Dao
interface MediaDAO {

    @Insert
    fun  insert(media: Media)

    @Update
    fun update(media: Media)

    @Query("SELECT * FROM media_table WHERE id_media=:idMedia")
    fun getProperty(idMedia: Long): Media?

    @Query("DELETE FROM media_table WHERE id_media=:idMedia")
    fun deleteProperty(idMedia: Long)

    @Query("SELECT *  FROM media_table ORDER BY id_media DESC LIMIT 1")
    fun getLastProperty(): Media?

    @Query("SELECT * FROM media_table ORDER BY id_media DESC")
    fun getAllProperty(): LiveData<List<Media>>

    @Query("SELECT photo FROM media_table WHERE id_property_media=:idProperty")
    fun getUriPhoto(idProperty: Long): List<Uri>?

    @Query("SELECT * FROM media_table WHERE id_property_media=:idProperty")
    fun getListMediaWithIdProperty(idProperty: Long): List<Media>?


}
