package com.tierriapps.myworkoutorganizer.feature_main.data.remote.dto

import com.tierriapps.myworkoutorganizer.feature_main.domain.models.Division
import com.tierriapps.myworkoutorganizer.feature_main.domain.models.Workout

data class WorkoutRemoteEntity(
    val ownerId: String = "",
    val localId: Int? = null,
    val name: String = "",
    val description: String = "",
    val divisions: List<Division> = listOf(),
    val trainingsDone: MutableList<Division> = mutableListOf()
)

fun Workout.toRemoteEntity(ownerId: String): WorkoutRemoteEntity {
    return WorkoutRemoteEntity(
        ownerId = ownerId,
        localId = this.id,
        name = this.name,
        description = this.description,
        divisions = this.divisions,
        trainingsDone = this.trainingsDone
    )
}