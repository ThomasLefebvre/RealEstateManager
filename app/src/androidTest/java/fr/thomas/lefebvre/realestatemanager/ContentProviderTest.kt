package fr.thomas.lefebvre.realestatemanager

import android.content.ContentResolver
import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import fr.thomas.lefebvre.realestatemanager.database.PropertyDatabase
import org.junit.Before
import org.junit.Test
import android.content.ContentUris
import android.content.ContentValues
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import fr.thomas.lefebvre.realestatemanager.provider.ContentProvider
import org.junit.Assert.assertEquals
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.notNullValue


class ContentProviderTest {

    lateinit var mContentResolver: ContentResolver

    companion object {
        val AGENT_ID: Long = System.currentTimeMillis()
        val PROPERTY_ID: Long = System.currentTimeMillis()
        val AGENT_ID_TEST:Long=1
    }

    // ----    SETUP TEST ----

    @Before
    fun setUpDatabase() {
        Room.inMemoryDatabaseBuilder(
            InstrumentationRegistry.getInstrumentation().context,
            PropertyDatabase::class.java
        )
            .allowMainThreadQueries()
            .build()
        mContentResolver = InstrumentationRegistry.getInstrumentation().context.contentResolver
    }

    // ----         AGENT PROVIDER ----
    @Test
    fun getAgentWhenNoItemInserted() {
        val cursor = mContentResolver.query(
            ContentUris.withAppendedId(
                ContentProvider.CONTENT_URI_AGENT,
                AGENT_ID
            ), null, null, null, null
        )
        assertThat(cursor, notNullValue())
        assertEquals(cursor!!.count, 1)
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
    }


    private fun initAgent(): ContentValues {//value of agent inserted
        val values = ContentValues()
        values.put("idAgent", AGENT_ID.toString())
        values.put("name", "Columbo")
        values.put("mail", "test@gmail.com")
        return values
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
        assertThat(cursor!!.count, `is`(1))
        cursor!!.close()
    }


    @Test
    fun instertAndGetProperty() {
        val uri = mContentResolver.insert(
            ContentProvider.CONTENT_URI_PROPERTY,
            initProperty()
        )//insert property with provider

        val cursor = mContentResolver.query(//init cursor for test
            ContentUris.withAppendedId(
                ContentProvider.CONTENT_URI_PROPERTY,
                AGENT_ID
            ), null, null, null, null
        )

        assertThat(cursor, notNullValue())
        assertThat(cursor!!.count, `is`(1))
        assertThat(cursor.moveToFirst(), `is`(true))
        assertThat(cursor.getString(cursor.getColumnIndexOrThrow("type")), `is`("STUDIO"))
    }


    private fun initProperty(): ContentValues {//value of property inserted
        val values = ContentValues()
        values.put("idProperty", PROPERTY_ID.toString())
        values.put("idAgent", AGENT_ID_TEST.toString())
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
}