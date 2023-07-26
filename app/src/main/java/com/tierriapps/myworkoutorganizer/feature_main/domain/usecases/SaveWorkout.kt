package com.tierriapps.myworkoutorganizer.feature_main.domain.usecases

import com.tierriapps.myworkoutorganizer.common.utils.Resource
import com.tierriapps.myworkoutorganizer.feature_main.domain.models.Workout
import kotlinx.coroutines.flow.flow

class SaveWorkout {
    suspend operator fun invoke(workout: Workout) = flow<Resource<Workout>> {

    }
}