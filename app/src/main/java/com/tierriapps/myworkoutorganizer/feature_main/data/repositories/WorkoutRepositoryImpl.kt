package com.tierriapps.myworkoutorganizer.feature_main.data.repositories

import com.tierriapps.myworkoutorganizer.common.utils.Resource
import com.tierriapps.myworkoutorganizer.feature_main.data.local.datasources.LocalUserPreferences
import com.tierriapps.myworkoutorganizer.feature_main.data.local.datasources.WorkoutLocalDAO
import com.tierriapps.myworkoutorganizer.feature_main.data.remote.datasources.WorkoutRemoteDAO
import com.tierriapps.myworkoutorganizer.feature_main.domain.models.Workout
import com.tierriapps.myworkoutorganizer.feature_main.domain.repositories.WorkoutRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class WorkoutRepositoryImpl @Inject constructor(
    private val remoteWorkoutDAO: WorkoutRemoteDAO,
    private val localWorkoutDAO: WorkoutLocalDAO,
    private val localPreferences: LocalUserPreferences
): WorkoutRepository {
    override fun getAllWorkouts(): Flow<Resource<List<Workout>>?> {
        TODO("Not yet implemented")
    }

    override fun getWorkoutById(): Flow<Resource<Workout?>> {
        TODO("Not yet implemented")
    }

    override fun insertWorkout(workout: Workout): Flow<Resource<Workout?>> {
        TODO("Not yet implemented")
    }

    override fun deleteWorkout(workout: Workout): Flow<Resource<Workout?>> {
        TODO("Not yet implemented")
    }

    override suspend fun getActualWorkoutId(): Resource<Int?> {
        TODO("Not yet implemented")
    }

    override suspend fun setActualWorkoutId(id: Int): Resource<Int?> {
        TODO("Not yet implemented")
    }

    override fun getUserId(): String {
        TODO("Not yet implemented")
    }

    override fun getUserName(): String {
        TODO("Not yet implemented")
    }

}