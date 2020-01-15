package fr.thomas.lefebvre.realestatemanager;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.Shadows;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowNetworkInfo;
import java.io.IOException;
import fr.thomas.lefebvre.realestatemanager.ui.screen.MainActivity;
import fr.thomas.lefebvre.realestatemanager.util.Utils;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertFalse;




@RunWith(RobolectricTestRunner.class)
@Config(sdk = Build.VERSION_CODES.O_MR1)
public class ConnectionTest {

    private ShadowNetworkInfo shadowOfActiveNetworkInfo;
    private MainActivity activity;

    @Before
    public void setUp() throws IOException {
        ConnectivityManager connectivityManager = getConnectivityManager();
        shadowOfActiveNetworkInfo = Shadows.shadowOf(connectivityManager.getActiveNetworkInfo());
        activity = Robolectric.setupActivity(MainActivity.class);


    }


    @Test
    public void testConnectionEnable() {

        shadowOfActiveNetworkInfo.setConnectionStatus(NetworkInfo.State.CONNECTED);
        assertTrue(Utils.isInternetAvailableCorrection(activity));


    }

    @Test
    public void testConnectionDisable(){
        shadowOfActiveNetworkInfo.setConnectionStatus(NetworkInfo.State.DISCONNECTED);
        assertFalse(Utils.isInternetAvailableCorrection(activity));

    }


    private ConnectivityManager getConnectivityManager() {
        return (ConnectivityManager) RuntimeEnvironment.application.getSystemService(Context.CONNECTIVITY_SERVICE);
    }
}