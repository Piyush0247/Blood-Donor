package jain.piyush.blooddonor.roomdatabase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Person::class], version = 2, exportSchema = false)
abstract class PersonDatabase :RoomDatabase(){
    abstract fun personDao():PersonDao

    companion object{
     @Volatile
     private var INSTANCE : PersonDatabase? = null
        fun getDatabase(context: Context): PersonDatabase {
            return INSTANCE ?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    PersonDatabase::class.java,
                    "person_database"
                ).fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance

            }

        }
    }
}