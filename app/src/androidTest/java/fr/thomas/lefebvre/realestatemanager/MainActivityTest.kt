package fr.thomas.lefebvre.realestatemanager

import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import fr.thomas.lefebvre.realestatemanager.ui.screen.MainActivity
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith







@RunWith(AndroidJUnit4::class)
@LargeTest
class MainActivityTest {

    @Rule @JvmField
    val mActivityTestRules= ActivityTestRule <MainActivity>(MainActivity::class.java)

    private lateinit var mActivity:MainActivity

    @Before
    fun setUp(){
        mActivity=mActivityTestRules.activity
    }

    @Test
    fun clickOnIconMap(){
        onView(withId(R.id.fragmentMap))
            .perform(click())

            onView(withId(R.id.mapViewMap)).check(matches(isDisplayed()))
    }

    @Test
    fun clickOnIconFilter(){
        onView(withId(R.id.filterProperty))
            .perform(click())

        Thread.sleep(1500)

        onView(withText("NO FILTER"))
            .perform(click())

//        onView(withId(R.id.material_text_button_no_filter)).check(matches(isDisplayed()))
    }

    @Test
    fun clickOnIconAddProperty(){


        onView(withId(R.id.addPropertyActivity))
            .perform(click())

        onView(withId(R.id.buttonSelectAddress)).check(matches(isDisplayed()))
    }
}