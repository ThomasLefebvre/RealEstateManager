package fr.thomas.lefebvre.realestatemanager.database.dao

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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

    @Query("SELECT id_property  FROM property_table ORDER BY creation_date DESC LIMIT 1")
    fun getLastPropertyID(): Long

    @Query("SELECT * FROM property_table ORDER BY creation_date DESC")
    fun getAllPropertyLiveData():LiveData<List<Property>>

    @Query("SELECT * FROM property_table ORDER BY creation_date DESC")
    fun getAllProperty():List<Property>


    @Query("SELECT * FROM property_table ORDER BY surface DESC")
    fun getAllPropertyBySurface():List<Property>


    @Query("SELECT * FROM property_table WHERE address LIKE :address AND price BETWEEN :minPrice AND :maxPrice AND number_room BETWEEN :minRoom AND :maxRoom AND surface BETWEEN :minSurface AND :maxSurface AND state_property=:sold AND creation_date >= :creationDate AND sale_date >= :soldDate AND school=:school AND sport=:sport AND transport=:transport AND parc=:parc AND type IN (:listType)" )
    fun getAllPropertyQuery(address:String,minPrice:Long,maxPrice:Long,minRoom:Int,maxRoom:Int,minSurface:Int,maxSurface:Int,sold:Boolean,creationDate:Long,soldDate:Long,school:Boolean,sport:Boolean,transport:Boolean,parc:Boolean,listType:List<String>):List<Property>
}