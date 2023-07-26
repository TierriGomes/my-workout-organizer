package com.tierriapps.myworkoutorganizer.feature_main.domain.usecases

import com.tierriapps.myworkoutorganizer.common.utils.Resource
import com.tierriapps.myworkoutorganizer.feature_main.domain.models.Workout
import kotlinx.coroutines.flow.flow

class SetActualWorkout {
    operator fun invoke(workout: Workout) = flow<Resource<Int>> {  }
}