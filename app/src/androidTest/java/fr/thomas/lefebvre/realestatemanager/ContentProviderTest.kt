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
import fr.thomas.lefebvre.realestatemanager.provider.AgentContentProvider
import junit.framework.Assert.assertEquals
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.notNullValue


class ContentProviderTest {

    lateinit var mContentResolver: ContentResolver

    companion object {
        val AGENT_ID: Long = 123
    }


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


    @Test
    fun getAgentWhenNoItemInserted() {
        val cursor = mContentResolver.query(
            ContentUris.withAppendedId(
                AgentContentProvider.CONTENT_URI,
                AGENT_ID
            ), null, null, null, null
        )
        assertThat(cursor, notNullValue())
        assertEquals(cursor!!.count, 1)
        cursor.close()
    }


    @Test
    fun instertAndGetAgent() {
        val uri=mContentResolver.insert(AgentContentProvider.CONTENT_URI,initAgent())//insert agent with provider

        val cursor = mContentResolver.query(//init cursor for test
            ContentUris.withAppendedId(
                AgentContentProvider.CONTENT_URI,
                AGENT_ID
            ), null, null, null, null
        )

        assertEquals(cursor, notNullValue())
        assertThat(cursor!!.count, `is`(1))
        assertThat(cursor.moveToFirst(), `is`(true))
        assertThat(cursor.getString(cursor.getColumnIndexOrThrow("mail")), `is`("jacko@gmail.com"))
    }


    private fun initAgent(): ContentValues {//value of agent inserted
        val values = ContentValues()
        values.put("idAgent", "123")
        values.put("name", "Jacques Chirac")
        values.put("mail", "jacko@gmail.com")
        return values
    }
}