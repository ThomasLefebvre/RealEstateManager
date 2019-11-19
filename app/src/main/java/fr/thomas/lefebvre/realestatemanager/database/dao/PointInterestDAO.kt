package fr.thomas.lefebvre.realestatemanager.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import fr.thomas.lefebvre.realestatemanager.database.PointInterest

@Dao
interface PointInterestDAO {
    @Insert
    fun  insert(pointInterest: PointInterest)

    @Update
    fun update(pointInterest: PointInterest)

    @Query("SELECT * FROM point_interest_table WHERE id_point_interest=:id")
    fun getProperty(id: Long): PointInterest?

    @Query("DELETE FROM point_interest_table WHERE id_point_interest=:id")
    fun deleteProperty(id: Long)

    @Query("SELECT *  FROM point_interest_table ORDER BY id_point_interest DESC LIMIT 1")
    fun getLastProperty(): PointInterest?

    @Query("SELECT * FROM point_interest_table ORDER BY id_point_interest DESC")
    fun getAllProperty(): LiveData<List<PointInterest>>

}
