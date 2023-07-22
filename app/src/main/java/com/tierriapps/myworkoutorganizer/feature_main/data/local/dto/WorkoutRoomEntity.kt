package com.tierriapps.myworkoutorganizer.feature_main.data.local.dto

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

import com.tierriapps.myworkoutorganizer.feature_main.domain.models.Division
import com.tierriapps.myworkoutorganizer.feature_main.domain.models.Workout

@Entity("workouts")
data class WorkoutRoomEntity(
    @PrimaryKey(autoGenerate = true) val id: Int?,
    val name: String = "",
    val description: String = "",
    val divisions: List<Division>,
    @ColumnInfo("trainings_done")
    val trainingsDone: MutableList<Division> = mutableListOf()
)

fun Workout.toRoomEntity(): WorkoutRoomEntity{
    return WorkoutRoomEntity(
        id = this.id,
        name = this.name,
        description = this.description,
        divisions = this.divisions,
        trainingsDone = this.trainingsDone
    )
}





class WorkoutRoomEntityTypeConverter(){
    private val gson = Gson()
    @TypeConverter
    fun divisionsListToJson(divisionsList: MutableList<Division>): String {
        return gson.toJson(divisionsList)
    }

    @TypeConverter
    fun jsonToDivisionsList(json: String): MutableList<Division> {
        val listType = object : TypeToken<MutableList<Division>>() {}.type
        return gson.fromJson(json, listType)
    }
}

