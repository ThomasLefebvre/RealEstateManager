package fr.thomas.lefebvre.realestatemanager;



import androidx.test.rule.ActivityTestRule;
import org.junit.Rule;
import org.junit.Test;
import fr.thomas.lefebvre.realestatemanager.ui.screen.MainActivity;
import fr.thomas.lefebvre.realestatemanager.util.Utils;
import kotlin.jvm.JvmField;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ConnectionTest  {




    @Rule
    @JvmField
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);




    @Test
    public void testConnection(){
    assertTrue(Utils.isInternetAvailableCorrection(mActivityTestRule.getActivity()));
    }

    @Test
    public void testConnectionAirPlaneMode()  {

        assertFalse(Utils.isInternetAvailableCorrection(mActivityTestRule.getActivity()));
    }










}
