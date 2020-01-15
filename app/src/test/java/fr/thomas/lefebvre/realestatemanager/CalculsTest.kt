package fr.thomas.lefebvre.realestatemanager

import fr.thomas.lefebvre.realestatemanager.util.calculLoan
import fr.thomas.lefebvre.realestatemanager.util.initMaxQuery
import fr.thomas.lefebvre.realestatemanager.util.initMinQuery
import org.junit.Test
import org.junit.Assert.assertEquals

class CalculsTest {


    @Test
     fun testCalculLoan(){
        val amount:Double=100000.0
        val term:Double=20.0
        val bring:Double=10000.0
        val interest:Double=1.9
        val listResult= calculLoan(amount,term,bring,interest)
        assertEquals("451",listResult[0])
        assertEquals("108240",listResult[1])
        assertEquals("18240",listResult[2])
    }

    @Test
    fun testInitMaxQuery(){
        val max:Long=50
        var query=""
        assertEquals(50, initMaxQuery(query,max))

        query = "49"
        assertEquals(49, initMaxQuery(query,max))

        query="51"
        assertEquals(50, initMaxQuery(query,max))

    }

    @Test
    fun testInitMinQuery(){
        var query=""
        assertEquals(0, initMinQuery(query))

        query="55"
        assertEquals(55, initMinQuery(query))
    }
}