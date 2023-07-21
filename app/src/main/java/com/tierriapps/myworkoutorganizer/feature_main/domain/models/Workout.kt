package com.tierriapps.myworkoutorganizer.feature_main.domain.models

import com.tierriapps.myworkoutorganizer.feature_main.data.local.dto.WorkoutRoomEntity
import com.tierriapps.myworkoutorganizer.feature_main.data.remote.dto.WorkoutRemoteEntity

data class Workout(
    val id: Int? = null,
    val name: String = "",
    val description: String = "",
    val divisions: List<Division>,
    val trainingsDone: MutableList<Division> = mutableListOf()
)

fun WorkoutRoomEntity.toWorkout(): Workout{
    return Workout(
        id = this.id,
        name = this.name,
        description = this.description,
        divisions = this.divisions,
        trainingsDone = this.trainingsDone
    )
}

fun WorkoutRemoteEntity.toWorkout(): Workout {
    return Workout(
        id = this.localId,
        name = this.name,
        description = this.description,
        divisions = this.divisions,
        trainingsDone = this.trainingsDone
    )
}