package fr.thomas.lefebvre.realestatemanager

import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import fr.thomas.lefebvre.realestatemanager.database.Agent
import fr.thomas.lefebvre.realestatemanager.database.Property
import fr.thomas.lefebvre.realestatemanager.database.PropertyDatabase
import fr.thomas.lefebvre.realestatemanager.database.dao.AgentDAO
import fr.thomas.lefebvre.realestatemanager.database.dao.PropertyDAO
import org.junit.After
import org.junit.Before
import org.junit.Assert.assertEquals
import org.junit.Test

class DatabaseTest {

    private var bdd:PropertyDatabase?=null
    private var agentDao:AgentDAO?=null
    private var propertyDao:PropertyDAO?=null

    private val ID_AGENT:Long=System.currentTimeMillis()

    private val ID_PROPERTY:Long=System.currentTimeMillis()

    // ----                      SETUP TEST                ------------

    @Before
    fun createBdd(){
        val context=InstrumentationRegistry.getInstrumentation().context//setup context
        bdd= Room.inMemoryDatabaseBuilder(context,PropertyDatabase::class.java).allowMainThreadQueries().build()//init bdd
        agentDao=bdd!!.agentDAO//init agent DAO
        propertyDao=bdd!!.propertyDAO//init property DAO
    }

    @After
    fun closeBdd(){
        bdd!!.close()
    }

    // ------                     AGENT TEST             -------------

    @Test
    fun insertAgentAndGetItemById(){
        val agent:Agent=Agent(ID_AGENT,"TestNameAgent","testMailAgent@mail.com")
        agentDao!!.insert(agent)
        val agentById=agentDao!!.getAgent(ID_AGENT)
        assertEquals(agent.name,agentById!!.name)
        assertEquals(agent.mail,agentById.mail)

    }

    @Test
    fun insertAgentAndGetByName(){
        val agent:Agent=Agent(ID_AGENT,"TestNameAgent","testMailAgent@mail.com")
        agentDao!!.insert(agent)
        val agentByName=agentDao!!.getAgentByName("TestNameAgent")
        assertEquals(agent.name,agentByName!!.name)
        assertEquals(agent.mail,agentByName.mail)
    }

    // -----                  PROPERTY TEST           --------------

    @Test
    fun insertPropertyAndGetItemById(){

        val agent:Agent=Agent(ID_AGENT,"TestNameAgent","testMailAgent@mail.com")
        agentDao!!.insert(agent)
        val property:Property=Property(ID_PROPERTY,"HOUSE",95000,95,5,
            "Great House","450 boulevard de la paix, Paris",
            -1.0,-1.0,true,true,true,true,
            false,0,0,ID_AGENT)
       propertyDao!!.insert(property)
        val propertyById=propertyDao!!.getProperty(ID_PROPERTY)
        assertEquals(property.address,propertyById!!.address)
        assertEquals(property.price,propertyById.price)

    }


}