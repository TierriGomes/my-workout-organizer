package com.tierriapps.myworkoutorganizer.feature_main.domain.usecases

import com.tierriapps.myworkoutorganizer.R
import com.tierriapps.myworkoutorganizer.common.utils.Resource
import com.tierriapps.myworkoutorganizer.common.utils.UiText
import com.tierriapps.myworkoutorganizer.feature_main.domain.models.Workout
import com.tierriapps.myworkoutorganizer.feature_main.domain.repositories.WorkoutRepository
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.last
import javax.inject.Inject

class SetActualWorkout @Inject constructor(val repository: WorkoutRepository) {
    operator fun invoke(workout: Workout) = flow<Resource<Int?>> {
        if (workout.id != null){
            val workoutExists = repository.getWorkoutById(workout.id).last().content
            if (workoutExists != null){
                emit(repository.setActualWorkoutId(workout.id))
            }else {
                emit(Resource.Error(null, UiText.StringResource(R.string.workout_not_saved)))
            }
        }else {
            emit(Resource.Error(null, UiText.StringResource(R.string.workout_not_saved)))
        }
    }
}