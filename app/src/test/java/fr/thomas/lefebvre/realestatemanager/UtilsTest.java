package fr.thomas.lefebvre.realestatemanager;


import org.junit.Test;


import java.text.DateFormat;
import java.text.SimpleDateFormat;

import fr.thomas.lefebvre.realestatemanager.util.Utils;

import static org.junit.Assert.assertEquals;

public class UtilsTest {

    private Utils utils=new Utils();




    @Test
    public void testFormatDate(){

        Long dateToday=new Long(System.currentTimeMillis());
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        assertEquals(dateFormat.format(dateToday),utils.getTodayDateCorrection());
    }

    @Test
    public void testConvertDollarToEuro(){
        int euro=812;
        int dollar=1000;
        assertEquals(euro,utils.convertDollarToEuro(1000));
    }

    @Test
    public void testConvertEuroToDollar(){
        int euro=1232;
        int dollar=1000;
        assertEquals(euro,utils.convertEuroToDollarCorrestion(1000));
    }
}
