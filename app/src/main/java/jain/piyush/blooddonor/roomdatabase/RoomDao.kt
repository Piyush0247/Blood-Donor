package jain.piyush.blooddonor.roomdatabase

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface PersonDao {
    @Insert
    suspend fun insertPerson(person: Person)
    @Query("SELECT * FROM persons WHERE isCompleted = 0")
    fun getAllPerson(): List<Person>
    @Query("SELECT * FROM persons WHERE isCompleted = 1")
    fun getCompletedPersons():Flow<List<Person>>
    @Update
    fun updatePerson(person: Person)
    @Delete
    suspend fun deletePerson(person: Person)
}