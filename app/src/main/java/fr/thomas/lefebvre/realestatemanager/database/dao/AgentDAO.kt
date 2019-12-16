package fr.thomas.lefebvre.realestatemanager.database.dao

import android.database.Cursor
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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
    fun getAgent(idAgent: Long): Agent?

    @Query("DELETE FROM agent_table WHERE id_agent=:idAgent")
    fun deleteAgent(idAgent: Long)

    @Query("SELECT name FROM AGENT_TABLE")
    fun getAllName():LiveData<List<String>>


    @Query("SELECT * FROM agent_table ORDER BY id_agent DESC")
    fun getAllAgent(): LiveData<List<Agent>>

    // ----      CONTENT PROVIDER ----

    @Query("SELECT * FROM agent_table WHERE id_agent=:idAgent")
    fun getAgentWithCursor(idAgent: Long):Cursor

    @Query("SELECT * FROM agent_table")
    fun getAllAgentWithCursor():Cursor

}
