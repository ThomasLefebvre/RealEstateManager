package fr.thomas.lefebvre.realestatemanager;


import org.junit.Test;


import java.text.DateFormat;
import java.text.SimpleDateFormat;

import fr.thomas.lefebvre.realestatemanager.util.Utils;

import static org.junit.Assert.assertEquals;

public class UtilsTest {





    @Test
    public void testFormatDate(){

        Long dateToday= System.currentTimeMillis();
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        assertEquals(dateFormat.format(dateToday),Utils.getTodayDateCorrection());
    }

    @Test
    public void testConvertDollarToEuro(){
        int euro=812;
        int dollar=1000;
        assertEquals(euro,Utils.convertDollarToEuro(dollar));
    }

    @Test
    public void testConvertEuroToDollar(){
        int euro=1232;
        int dollar=1000;
        assertEquals(euro,Utils.convertEuroToDollarCorrestion(dollar));
    }
}
