package com.tierriapps.myworkoutorganizer.feature_main.domain.repositories

import com.tierriapps.myworkoutorganizer.common.utils.Resource
import com.tierriapps.myworkoutorganizer.feature_main.domain.models.Workout
import kotlinx.coroutines.flow.Flow

interface WorkoutRepository {

    fun getAllWorkouts(): Flow<Resource<List<Workout>>?>

    fun getWorkoutById(): Flow<Resource<Workout?>>

    fun insertWorkout(workout: Workout): Flow<Resource<Workout?>>

    fun deleteWorkout(workout: Workout): Flow<Resource<Workout?>>

    suspend fun getActualWorkoutId(): Resource<Int?>

    suspend fun setActualWorkoutId(id: Int): Resource<Int?>

    fun getUserId(): String

    fun getUserName(): String

}