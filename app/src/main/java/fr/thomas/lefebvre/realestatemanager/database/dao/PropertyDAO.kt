package fr.thomas.lefebvre.realestatemanager.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import fr.thomas.lefebvre.realestatemanager.database.Property

@Dao
interface PropertyDAO{

    @Insert
    fun  insert(property:Property)

    @Update
    fun update(property:Property)

    @Query("SELECT * FROM property_table WHERE id_property=:idProperty")
    fun getProperty(idProperty: Long):Property?

    @Query("DELETE FROM property_table WHERE id_property=:idProperty")
    fun deleteProperty(idProperty: Long)

    @Query("SELECT *  FROM property_table ORDER BY id_property DESC LIMIT 1")
    fun getLastProperty():LiveData<Property>

    @Query("SELECT * FROM property_table ORDER BY creation_date DESC")
    fun getAllProperty():LiveData<List<Property>>
}