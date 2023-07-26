package com.tierriapps.myworkoutorganizer.feature_main.domain.usecases

import com.tierriapps.myworkoutorganizer.feature_main.domain.models.Division
import com.tierriapps.myworkoutorganizer.feature_main.domain.models.Workout

class CreateWorkout {
    operator fun invoke(
        name: String,
        description: String,
        divisions: List<Division>,
    ): Workout {

    }
}