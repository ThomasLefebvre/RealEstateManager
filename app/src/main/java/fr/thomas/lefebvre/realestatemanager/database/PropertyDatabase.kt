package fr.thomas.lefebvre.realestatemanager.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import fr.thomas.lefebvre.realestatemanager.database.dao.*

@Database(
    entities = [Property::class, Media::class, PointInterest::class, Agent::class],
    version = 1,
    exportSchema = false
)

abstract class PropertyDatabase : RoomDatabase() {

    abstract val propertyDAO: PropertyDAO
    abstract val mediaDAO: MediaDAO
    abstract val pointInterest: PointInterestDAO
    abstract val agentDAO: AgentDAO

    companion object {

        @Volatile
        private var INSTANCE: PropertyDatabase? = null

        fun getInstance(context: Context): PropertyDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
            }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                PropertyDatabase::class.java,
                "property_database"
            )
                .fallbackToDestructiveMigration()
                .build()


    }
}