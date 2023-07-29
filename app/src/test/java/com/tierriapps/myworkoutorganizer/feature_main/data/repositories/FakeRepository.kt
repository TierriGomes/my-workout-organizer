package com.tierriapps.myworkoutorganizer.feature_main.data.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tierriapps.myworkoutorganizer.R
import com.tierriapps.myworkoutorganizer.common.utils.Resource
import com.tierriapps.myworkoutorganizer.common.utils.UiText
import com.tierriapps.myworkoutorganizer.feature_main.domain.models.Workout
import com.tierriapps.myworkoutorganizer.feature_main.domain.repositories.WorkoutRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeRepository: WorkoutRepository {
    var shouldReturnSuccess = true
    var listOfWorkouts = listOf(
        Workout(1, "name1", "description"),
        Workout(2, "name2", "description")
    )

    var getAllWorkoutCalls = 0
    var getWorkoutByIdCalls = 0
    var insertWorkoutCalls = 0
    var deleteWorkoutCalls = 0
    var getActualWorkoutCalls = 0
    var setActualWorkoutCalls = 0
    var getUserIdCalls = 0
    var getUserNameCalls = 0

    override fun getAllWorkouts(): Flow<Resource<List<Workout>>?> = flow {
        getAllWorkoutCalls ++
        emit(Resource.Loading())
        if (shouldReturnSuccess && listOfWorkouts.isNotEmpty()){
            emit(Resource.Success(listOfWorkouts, UiText.StringResource(R.string.success)))
        }
        else {
            emit(Resource.Error(null, UiText.StringResource(R.string.error)))
        }
    }

    override fun getWorkoutById(id: Int): Flow<Resource<Workout?>> = flow {
        getWorkoutByIdCalls ++
        emit(Resource.Loading())
        if (shouldReturnSuccess && listOfWorkouts.isNotEmpty()){
            emit(Resource.Success(listOfWorkouts[1], UiText.StringResource(R.string.success)))
        }
        else {
            emit(Resource.Error(null, UiText.StringResource(R.string.error)))
        }
    }

    override fun insertWorkout(workout: Workout): Flow<Resource<Workout?>> = flow {
        insertWorkoutCalls ++
        emit(Resource.Loading())
        if (shouldReturnSuccess){
            emit(Resource.Success(listOfWorkouts[1], UiText.StringResource(R.string.success)))
        }
        else {
            emit(Resource.Error(null, UiText.StringResource(R.string.error)))
        }
    }

    override fun deleteWorkout(workout: Workout): Flow<Resource<Workout?>>  = flow {
        deleteWorkoutCalls ++
        emit(Resource.Loading())
        if (shouldReturnSuccess){
            emit(Resource.Success(listOfWorkouts[1], UiText.StringResource(R.string.success)))
        }
        else {
            emit(Resource.Error(null, UiText.StringResource(R.string.error)))
        }
    }

    override suspend fun getActualWorkoutId(): Resource<Int?> {
        getActualWorkoutCalls ++
        if (shouldReturnSuccess){
            return Resource.Success(1, UiText.StringResource(R.string.success))
        }
        else {
            return Resource.Error(null, UiText.StringResource(R.string.error))
        }
    }

    override suspend fun setActualWorkoutId(id: Int): Resource<Int?> {
        setActualWorkoutCalls ++
        if (shouldReturnSuccess){
            return Resource.Success(1, UiText.StringResource(R.string.success))
        }
        else {
            return Resource.Error(null, UiText.StringResource(R.string.error))
        }
    }

    override fun getUserId(): LiveData<String> {
        getUserIdCalls ++
        return MutableLiveData("userId")
    }

    override fun getUserName(): LiveData<String> {
        getUserNameCalls ++
        return MutableLiveData("userName")
    }
}