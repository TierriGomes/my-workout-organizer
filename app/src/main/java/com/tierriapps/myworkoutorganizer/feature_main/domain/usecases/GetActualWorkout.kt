package com.tierriapps.myworkoutorganizer.feature_main.domain.usecases


import com.tierriapps.myworkoutorganizer.common.utils.Resource
import com.tierriapps.myworkoutorganizer.feature_main.domain.models.Workout
import com.tierriapps.myworkoutorganizer.feature_main.domain.repositories.WorkoutRepository
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.last
import javax.inject.Inject

class GetActualWorkout @Inject constructor(val repository: WorkoutRepository) {
    operator fun invoke() = flow<Resource<Workout?>> {
        emit(Resource.Loading())
        val id = repository.getActualWorkoutId()
        if (id.content != null) {
            emit(repository.getWorkoutById(id.content).last())
        }else {
            emit(Resource.Error(null, id.message!!))
        }
    }
}