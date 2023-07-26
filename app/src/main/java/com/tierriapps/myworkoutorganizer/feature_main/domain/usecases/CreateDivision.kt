package com.tierriapps.myworkoutorganizer.feature_main.domain.usecases

import com.tierriapps.myworkoutorganizer.feature_main.domain.models.Division
import com.tierriapps.myworkoutorganizer.feature_main.domain.models.Exercise

class CreateDivision {
    operator fun invoke(
        name: String,
        description: String,
        listOfExercises: List<Exercise>? = null
    ): Division {


    }
}