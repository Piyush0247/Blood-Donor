package jain.piyush.blooddonor.roomdatabase

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "persons")
data class Person(
    @PrimaryKey(autoGenerate = true)
    val id:Int = 0,
    val name:String,
    val age:String,
    val mobile:String,
    val  bloodGroup:String,
    val location:String,
    val isCompleted:Boolean = false
)