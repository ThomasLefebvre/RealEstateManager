package fr.thomas.lefebvre.realestatemanager

import android.content.ContentResolver
import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import fr.thomas.lefebvre.realestatemanager.database.PropertyDatabase
import org.junit.Before
import org.junit.Test
import android.content.ContentUris
import android.content.ContentValues
import android.content.UriMatcher
import android.net.Uri
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import fr.thomas.lefebvre.realestatemanager.provider.ContentProvider
import org.junit.Assert.assertEquals
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.notNullValue
import org.junit.After


class ContentProviderTest {

    lateinit var mContentResolver: ContentResolver

    private var bdd: PropertyDatabase? = null

    companion object {
        val AGENT_ID: Long = 100
        val PROPERTY_ID: Long = 200

    }

    // ----    SETUP TEST ----

    @Before
    fun setUpDatabase() {
        bdd = Room.inMemoryDatabaseBuilder(
            InstrumentationRegistry.getInstrumentation().context,
            PropertyDatabase::class.java
        )
            .allowMainThreadQueries()
            .build()
        mContentResolver = InstrumentationRegistry.getInstrumentation().context.contentResolver
    }


    @After
    fun closeBdd() {
        bdd!!.close()
    }


    // ----         PROPERTY PROVIDER ----

    @Test
    fun getPropertyWhenNoItemInserted() {
        val cursor = mContentResolver.query(
            ContentUris.withAppendedId(
                ContentProvider.CONTENT_URI_PROPERTY,
                PROPERTY_ID
            ), null, null, null, null
        )
        assertThat(cursor, notNullValue())
        assertThat(cursor!!.count, `is`(0))
        cursor!!.close()
    }


    @Test
    fun instertAndGetProperty() {
        val uriAgent = mContentResolver.insert(
            ContentProvider.CONTENT_URI_AGENT,
            initAgent()
        )//insert agent with provider

        val uriProperty = mContentResolver.insert(
            ContentProvider.CONTENT_URI_PROPERTY,
            initProperty()
        )//insert property with provider

        val cursor = mContentResolver.query(//init cursor for test
            ContentUris.withAppendedId(
                ContentProvider.CONTENT_URI_PROPERTY,
                PROPERTY_ID
            ), null, null, null, null
        )

        assertThat(cursor, notNullValue())
        assertThat(cursor!!.count, `is`(1))
        assertThat(cursor.moveToFirst(), `is`(true))
        assertThat(cursor.getString(cursor.getColumnIndexOrThrow("type")), `is`("STUDIO"))

        val uriDeleteProperty = mContentResolver.delete(
            Uri.parse(ContentProvider.CONTENT_URI_PROPERTY.toString() + "/$PROPERTY_ID"),
            null,
            null
        )//delete property

        val uriDeleteAgent = mContentResolver.delete(
            Uri.parse(ContentProvider.CONTENT_URI_AGENT.toString() + "/$AGENT_ID")
            , null, null//delete agent
        )
    }


    private fun initProperty(): ContentValues {//value of property inserted
        val values = ContentValues()
        values.put("idProperty", PROPERTY_ID.toString())
        values.put("idAgent", AGENT_ID.toString())
        values.put("type", "STUDIO")
        values.put("price", "99999")
        values.put("surface", "99")
        values.put("numberRoom", "9")
        values.put("description", "Trés belle maison dans le 9eme.")
        values.put("address", "99 street")
        values.put("lat", "-1")
        values.put("lng", "-1")
        values.put("parc", "true")
        values.put("sport", "true")
        values.put("transport", "true")
        values.put("school", "true")
        values.put("stateProperty", "false")
        values.put("creationDate", "0")


        return values
    }

    //     ----         AGENT PROVIDER ----
    @Test
    fun getAgentWhenNoItemInserted() {
        val cursor = mContentResolver.query(
            ContentUris.withAppendedId(
                ContentProvider.CONTENT_URI_AGENT,
                AGENT_ID
            ), null, null, null, null
        )
        assertThat(cursor, notNullValue())
        assertEquals(cursor!!.count, 0)
        cursor!!.close()
    }


    @Test
    fun instertAndGetAgent() {
        val uri = mContentResolver.insert(
            ContentProvider.CONTENT_URI_AGENT,
            initAgent()
        )//insert agent with provider

        val cursor = mContentResolver.query(//init cursor for test
            ContentUris.withAppendedId(
                ContentProvider.CONTENT_URI_AGENT,
                AGENT_ID
            ), null, null, null, null
        )

        assertThat(cursor, notNullValue())
        assertThat(cursor!!.count, `is`(1))
        assertThat(cursor.moveToFirst(), `is`(true))
        assertThat(cursor.getString(cursor.getColumnIndexOrThrow("mail")), `is`("test@gmail.com"))


        val uriDelete = mContentResolver.delete(
            Uri.parse(ContentProvider.CONTENT_URI_AGENT.toString() + "/$AGENT_ID")
            , null, null
        )
    }


    private fun initAgent(): ContentValues {//value of agent inserted
        val values = ContentValues()
        values.put("idAgent", AGENT_ID.toString())
        values.put("name", "Bill Gates")
        values.put("mail", "test@gmail.com")
        return values
    }


}