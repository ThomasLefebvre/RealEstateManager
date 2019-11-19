package fr.thomas.lefebvre.realestatemanager.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import fr.thomas.lefebvre.realestatemanager.database.Agent


@Dao
interface AgentDAO {

    @Insert
    fun  insert(agent: Agent)

    @Update
    fun update(agent: Agent)

    @Query("SELECT * FROM agent_table WHERE id_agent=:idAgent")
    fun getProperty(idAgent: Long): Agent?

    @Query("DELETE FROM agent_table WHERE id_agent=:idAgent")
    fun deleteProperty(idAgent: Long)

    @Query("SELECT *  FROM agent_table ORDER BY id_agent DESC LIMIT 1")
    fun getLastProperty(): Agent?

    @Query("SELECT * FROM agent_table ORDER BY id_agent DESC")
    fun getAllProperty(): LiveData<List<Agent>>

}
