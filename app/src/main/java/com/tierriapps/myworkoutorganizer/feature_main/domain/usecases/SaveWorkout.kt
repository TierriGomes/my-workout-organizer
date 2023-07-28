package com.tierriapps.myworkoutorganizer.feature_main.domain.usecases

import com.tierriapps.myworkoutorganizer.common.utils.Resource
import com.tierriapps.myworkoutorganizer.feature_main.domain.models.Workout
import com.tierriapps.myworkoutorganizer.feature_main.domain.repositories.WorkoutRepository
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SaveWorkout @Inject constructor(val repository: WorkoutRepository) {
    operator fun invoke(workout: Workout) = repository.insertWorkout(workout)
}