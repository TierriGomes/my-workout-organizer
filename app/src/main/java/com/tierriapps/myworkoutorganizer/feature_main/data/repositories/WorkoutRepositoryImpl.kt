package com.tierriapps.myworkoutorganizer.feature_main.data.repositories

import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tierriapps.myworkoutorganizer.R
import com.tierriapps.myworkoutorganizer.common.utils.Resource
import com.tierriapps.myworkoutorganizer.common.utils.UiText
import com.tierriapps.myworkoutorganizer.common.values.Constants
import com.tierriapps.myworkoutorganizer.feature_main.data.local.datasources.LocalUserPreferences
import com.tierriapps.myworkoutorganizer.feature_main.data.local.datasources.WorkoutLocalDAO
import com.tierriapps.myworkoutorganizer.feature_main.data.local.dto.toRoomEntity
import com.tierriapps.myworkoutorganizer.feature_main.data.remote.datasources.WorkoutRemoteDAO
import com.tierriapps.myworkoutorganizer.feature_main.data.remote.dto.toRemoteEntity
import com.tierriapps.myworkoutorganizer.feature_main.domain.models.Workout
import com.tierriapps.myworkoutorganizer.feature_main.domain.models.toWorkout
import com.tierriapps.myworkoutorganizer.feature_main.domain.repositories.WorkoutRepository
import com.tierriapps.myworkoutorganizer.feature_main.utils.MyExceptionsNoWorkoutsFound
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class WorkoutRepositoryImpl @Inject constructor(
    private val remoteWorkoutDAO: WorkoutRemoteDAO,
    private val localWorkoutDAO: WorkoutLocalDAO,
    private val localPreferences: LocalUserPreferences
): WorkoutRepository {
    override fun getAllWorkouts(): Flow<Resource<List<Workout>>?> = flow {
        emit(Resource.Loading())
        try {
            val localWorkouts = localWorkoutDAO.getAllWorkoutEntities()
            if (localWorkouts.isNotEmpty()){
                emit(
                    Resource.Success(
                        localWorkouts.map { it.toWorkout() },
                        UiText.StringResource(R.string.workouts_loaded)))
            }else {
                val remoteWorkouts = remoteWorkoutDAO.getAllWorkoutEntities(Constants.USER_ID)
                if (remoteWorkouts.isEmpty()){
                    throw MyExceptionsNoWorkoutsFound("Workout not found")
                }
                for (i in remoteWorkouts.map { it.toWorkout() }){
                    localWorkoutDAO.insertWorkoutEntity(i.toRoomEntity())
                }
                emit(
                    Resource.Success(
                        remoteWorkouts.map { it.toWorkout() },
                        UiText.StringResource(R.string.workouts_loaded)
                    )
                )
            }
        }catch(e: Exception) {
            emit(Resource.Error(null, UiText.StringResource(R.string.error_cannot_find_workouts)))
        }
    }

    override fun getWorkoutById(id: Int): Flow<Resource<Workout?>> = flow {
        emit(Resource.Loading())
        try {
            val workout = localWorkoutDAO.getWorkoutEntityByID(id)!!.toWorkout()
            emit(Resource.Success(workout, UiText.StringResource(R.string.workouts_loaded)))
        }catch (e: Exception){
            emit(Resource.Error(null, UiText.StringResource(R.string.error_cannot_find_workouts)))
        }
    }

    override fun insertWorkout(workout: Workout): Flow<Resource<Workout?>> = flow {
        emit(Resource.Loading())
        var canLocalSave = false
        try {
            localWorkoutDAO.insertWorkoutEntity(workout.toRoomEntity())
            canLocalSave = true
            if (workout.id != null){
                remoteWorkoutDAO.insertWorkoutEntity(workout.toRemoteEntity(Constants.USER_ID))
            }else {
                val workoutR = localWorkoutDAO.getAllWorkoutEntities().last().toWorkout()
                remoteWorkoutDAO.insertWorkoutEntity(workoutR.toRemoteEntity(Constants.USER_ID))
            }
            emit(Resource.Success(workout, UiText.StringResource(R.string.changes_saved)))
        }catch (e: Exception){
            if (canLocalSave){
                emit(Resource.Success(workout, UiText.StringResource(R.string.cannot_save_changes_in_cloud)))
                localPreferences.setPendingTasksInRemote(true)
            }else {
                emit(Resource.Error(workout, UiText.StringResource(R.string.cannot_save_workout)))
            }
        }
    }

    override fun deleteWorkout(workout: Workout): Flow<Resource<Workout?>> = flow {
        emit(Resource.Loading())
        var canLocalDelete = false
        try {
            try {
                localWorkoutDAO.deleteWorkoutEntity(workout.toRoomEntity())
                canLocalDelete = true
            }catch (_: Exception){}
            remoteWorkoutDAO.deleteWorkoutEntity(workout.toRemoteEntity(Constants.USER_ID))
            emit(Resource.Success(workout, UiText.StringResource(R.string.changes_saved)))
        }catch (e: Exception){
            if (canLocalDelete){
                emit(Resource.Error(workout, UiText.StringResource(R.string.cannot_save_changes_in_cloud)))
                localPreferences.setPendingTasksInRemote(true)
            }else {
                emit(Resource.Error(workout, UiText.StringResource(R.string.error_cannot_find_workouts)))
            }
        }
    }

    override suspend fun getActualWorkoutId(): Resource<Int?> {
        return try {
            val result = localPreferences.getActualWorkoutId()
            if (result == null){
                Resource.Error(
                    null,
                    UiText.StringResource(R.string.actual_workout_not_defined)
                )
            }else {
                Resource.Success(result, UiText.StringResource(R.string.success))
            }
        }catch (e: Exception){
            Resource.Error(null, UiText.StringDynamic(e.message.toString()))
        }
    }

    override suspend fun setActualWorkoutId(id: Int): Resource<Int?> {
        return try {
            localPreferences.setActualWorkoutId(id)
            Resource.Success(id, UiText.StringResource(R.string.success))
        }catch (e: Exception){
            Resource.Error(null, UiText.StringDynamic(e.message.toString()))
        }
    }

    override fun getUserId(): LiveData<String> {
        return MutableLiveData(Constants.USER_ID)
    }

    override fun getUserName(): LiveData<String> {
        return MutableLiveData(Constants.USER_NAME)
    }

    override suspend fun cleanLocalData(){
        localWorkoutDAO.deleteAll()
        localPreferences.clearUserPreferences()
    }

}