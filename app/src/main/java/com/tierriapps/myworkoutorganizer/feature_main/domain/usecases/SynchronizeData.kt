package com.tierriapps.myworkoutorganizer.feature_main.domain.usecases

import com.tierriapps.myworkoutorganizer.common.values.Constants
import com.tierriapps.myworkoutorganizer.feature_main.data.local.datasources.WorkoutLocalDAO
import com.tierriapps.myworkoutorganizer.feature_main.data.local.dto.toRoomEntity
import com.tierriapps.myworkoutorganizer.feature_main.data.remote.datasources.WorkoutRemoteDAO
import com.tierriapps.myworkoutorganizer.feature_main.data.remote.dto.toRemoteEntity
import com.tierriapps.myworkoutorganizer.feature_main.data.repositories.PreferencesRepositoryImpl
import com.tierriapps.myworkoutorganizer.feature_main.domain.models.toWorkout
import com.tierriapps.myworkoutorganizer.feature_main.domain.repositories.WorkoutRepository
import kotlinx.coroutines.flow.last
import javax.inject.Inject

class SynchronizeData @Inject constructor(
    private val preferencesRepositoryImpl: PreferencesRepositoryImpl,
    private val localData: WorkoutLocalDAO,
    private val remoteDao: WorkoutRemoteDAO
) {
    suspend operator fun invoke(){
        val workouts = localData.getAllWorkoutEntities().map { it.toWorkout() }
        print("synchronize data: ${workouts.toString()}")
        if (workouts.isEmpty()) {
            val workoutsFromRemote = remoteDao.getAllWorkoutEntities(Constants.USER_ID)
            if(preferencesRepositoryImpl.isTherePendingTasks()){
                for (w in workoutsFromRemote){
                    remoteDao.deleteWorkoutEntity(w)
                }
            } else {
                for (w in workoutsFromRemote){
                    localData.insertWorkoutEntity(w.toWorkout().toRoomEntity())
                }
            }
        } else if (preferencesRepositoryImpl.isTherePendingTasks()){
            val workoutsFromRemote = remoteDao.getAllWorkoutEntities(Constants.USER_ID)
            for (w in workoutsFromRemote){
                remoteDao.deleteWorkoutEntity(w)
            }
            for (w in workouts){
                remoteDao.insertWorkoutEntity(w.toRemoteEntity(Constants.USER_ID))
            }
        }
    }
}