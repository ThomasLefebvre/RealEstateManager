package fr.thomas.lefebvre.realestatemanager

import fr.thomas.lefebvre.realestatemanager.util.*
import org.junit.Test
import org.junit.Assert.assertEquals


class FormatersTest(){

    @Test
    fun testFormatSurface(){

        var surface:Int?=50
        assertEquals("50 m²", formatSurfaceToStringSurface(surface))

        surface=null
        assertEquals("To inform surface", formatSurfaceToStringSurface(surface))
    }


    @Test
    fun testFormatPrice(){

        var price:Long?=50000
        assertEquals("50000 $", formatPriceToStringPriceDollar(price))

        price=null
        assertEquals("To inform price", formatPriceToStringPriceDollar(price))
    }


    @Test
    fun testFormatNumberRoom(){

        var numberRoom:Int?=5
        assertEquals("5 rooms", formatNumberRoomToString(numberRoom))

        numberRoom=null
        assertEquals("To inform number room", formatNumberRoomToString(numberRoom))
    }



    @Test
    fun testFormatDescription(){

        var description:String?="Great house in the center of Chicago"
        assertEquals("Great house in the center of Chicago", formatStringDescription(description))

        description=null
        assertEquals("Complete the description for users", formatStringDescription(description))
    }


    @Test
    fun testFormatDateLongToString(){

        val date:Long=0
        assertEquals("01/01/1970", formatDateLongToString(date))


    }

    @Test
    fun testConvertDollarToEuroAround(){
        val dollar:Long=1000
        assertEquals(812, convertDollarToEuro(dollar))
    }


}


