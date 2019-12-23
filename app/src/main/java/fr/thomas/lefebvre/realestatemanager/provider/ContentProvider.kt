package fr.thomas.lefebvre.realestatemanager.provider

import android.content.ContentProvider
import android.content.ContentUris
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import fr.thomas.lefebvre.realestatemanager.database.PropertyDatabase

class ContentProvider : ContentProvider() {

    private var myDB: PropertyDatabase? = null

    private val CODE_AGENT_DIR = 1
    private val CODE_AGENT_ITEM = 2
    private val CODE_PROPERTY_DIR = 3
    private val CODE_PROPERTY_ITEM = 4
    private val URIMatcher = UriMatcher(UriMatcher.NO_MATCH)

    companion object {

        val AUTHORITY: String = "fr.thomas.lefebvre.realestatemanager.provider"
        val TABLE_NAME_AGENT: String = "agent_table"
        val TABLE_NAME_PROPERTY: String = "property_table"
        val CONTENT_URI_AGENT: Uri = Uri.parse("content://$AUTHORITY/$TABLE_NAME_AGENT")
        val CONTENT_URI_PROPERTY: Uri = Uri.parse("content://$AUTHORITY/$TABLE_NAME_PROPERTY")
    }


    init {
        URIMatcher.addURI(AUTHORITY, TABLE_NAME_AGENT, CODE_AGENT_DIR)
        URIMatcher.addURI(AUTHORITY, "$TABLE_NAME_AGENT/#", CODE_AGENT_ITEM)
        URIMatcher.addURI(AUTHORITY, TABLE_NAME_PROPERTY, CODE_PROPERTY_DIR)
        URIMatcher.addURI(AUTHORITY, "$TABLE_NAME_PROPERTY/#", CODE_PROPERTY_ITEM)
    }


    override fun insert(uri: Uri, contentValue: ContentValues?): Uri? {

        if (context != null) {
            when (URIMatcher.match(uri)) {

                CODE_PROPERTY_DIR -> {
                    val db = PropertyDatabase.getInstance(context!!).propertyDAO
                    val newProperty = propertyFromContentValues(contentValue!!)
                    db.insert(newProperty)
                    val id = newProperty.idProperty
                    return ContentUris.withAppendedId(uri, id)
                }

                CODE_PROPERTY_ITEM -> {
                    throw java.lang.IllegalArgumentException("Dont need id for insert")

                }
                CODE_AGENT_DIR -> {
                    val db = PropertyDatabase.getInstance(context!!).agentDAO
                    val newAgent = agentFromContentValues(contentValue!!)
                    db.insert(newAgent)
                    val id = newAgent.idAgent
                    return ContentUris.withAppendedId(uri, id)
                }

                CODE_AGENT_ITEM -> {
                    throw java.lang.IllegalArgumentException("dont need id for insert")

                }
                else -> throw IllegalArgumentException("Wrong information $uri")
            }
        } else throw java.lang.IllegalArgumentException("Failed to query row $uri")


    }

    override fun query(
        uri: Uri,
        p1: Array<out String>?,
        p2: String?,
        p3: Array<out String>?,
        p4: String?
    ): Cursor? {
        val db: PropertyDatabase = PropertyDatabase.getInstance(context!!)
        var cursor: Cursor? = null
        val id = uri.lastPathSegment
        if (context != null) {

            when (URIMatcher.match(uri)) {

                CODE_AGENT_DIR -> {
                    cursor = db.agentDAO.getAllAgentWithCursor()
                }

                CODE_AGENT_ITEM -> {
                    cursor = db.agentDAO.getAgentWithCursor(id!!.toLong())
                }
                CODE_PROPERTY_DIR -> {
                    cursor = db.propertyDAO.getAllPropertyWithCursor()
                }

                CODE_PROPERTY_ITEM -> {
                    cursor = db.propertyDAO.getPropertyWithCursor(id!!.toLong())
                }
                else -> throw IllegalArgumentException("Wrong information $uri")
            }
        }



        return cursor

    }

    override fun onCreate(): Boolean {
        return true
    }

    override fun update(uri: Uri, p1: ContentValues?, p2: String?, p3: Array<out String>?): Int {
        throw IllegalArgumentException("Impossible to update $uri")
    }

    override fun delete(p0: Uri, p1: String?, p2: Array<out String>?): Int {
        throw IllegalArgumentException("Impossible to delete data")
    }

    override fun getType(uri: Uri): String? {
        val type: String
        when (URIMatcher.match(uri)) {
            CODE_AGENT_ITEM -> {
                type = "vnd.android.cursor.item/$AUTHORITY.$TABLE_NAME_AGENT"
            }
            CODE_AGENT_DIR -> {
                type = "vnd.android.cursor.dir/$AUTHORITY.$TABLE_NAME_AGENT"
            }
            CODE_PROPERTY_ITEM -> {
                type = "vnd.android.cursor.item/${AUTHORITY}.${TABLE_NAME_PROPERTY}"
            }
            CODE_PROPERTY_DIR -> {
                type = "vnd.android.cursor.dir/${AUTHORITY}.${TABLE_NAME_PROPERTY}"
            }
            else -> throw java.lang.IllegalArgumentException("Wrong information $uri")
        }
        return type
    }
}