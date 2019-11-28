package fr.thomas.lefebvre.realestatemanager.database

import android.net.Uri
import androidx.room.*
import java.util.*
import androidx.room.ColumnInfo
import androidx.room.PrimaryKey


@Entity(
    tableName = "property_table",
    foreignKeys = [ForeignKey(
        entity = Agent::class,
        parentColumns = ["id_agent"],
        childColumns = ["id_agent_property"]
    )]
)
data class Property(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id_property")
    var idProperty: Long = 0L,

    var type: String = "Loft",

    var price: Float = 0f,

    @ColumnInfo(name = "number_room")
    var numberRoom: Int = 0,

    var description: String = "",

    @Embedded
    var address: Address? = null,
    @ColumnInfo(name = "state_property")
    var stateProperty: Boolean = false,

    @ColumnInfo(name = "creation_date")

    var creationDate: Long=0L,

    @ColumnInfo(name = "sale_date")
    var saleDate: Long=0L,

    @ColumnInfo(name = "id_agent_property",index = true)
    var idAgent: Long = 0L


)

@Entity(
    tableName = "media_table",
    foreignKeys = [ForeignKey(
        entity = Property::class,
        parentColumns = ["id_property"],
        childColumns = ["id_property_media"]
    )]
)
data class Media(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id_media")
    var idMedia: Long = 0L,

    var photo: Uri,

    @ColumnInfo(name = "id_property_media",index = true)
    var idProperty: Long
)


data class Address(


    var city: String = "",

    @ColumnInfo(name = "postal_code")
    var postalCode: Int = 0,

    @ColumnInfo(name = "type_way")
    var typeWay: String = "",

    @ColumnInfo(name = "name_way")
    var nameWay: String = "",

    @ColumnInfo(name = "num_way")
    var numWay: Int = 0,

    var complement: String? = null,
    var state: String = ""
)

@Entity(tableName = "point_interest_table")
data class PointInterest(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id_point_interest")
    var idPointInterest: Long = 0L,

    var name: String = "",

    var type: String = ""
)

@Entity(
    tableName = "point_interest_property_table",
    foreignKeys = [ForeignKey(
        entity = Property::class,
        parentColumns = ["id_property"],
        childColumns = ["id_property_compo"]
    ), ForeignKey(
        entity = PointInterest::class,
        parentColumns = ["id_point_interest"],
        childColumns = ["id_point_interest_compo"]
    )]
)
data class PointInterestProperty(

    @ColumnInfo(name = "id_point_interest_compo")
    var idPointInterest: Long = 0L,

    @ColumnInfo(name = "id_property_compo")
    var idProperty: Long = 0L
)

@Entity(tableName = "agent_table")
data class Agent(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id_agent")
    var idAgent: Long = 0L,

    var name: String = "",

    var mail: String = ""

)


