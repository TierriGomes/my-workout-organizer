package com.tierriapps.myworkoutorganizer.feature_main.domain.usecases

import com.tierriapps.myworkoutorganizer.feature_main.domain.models.Exercise
import com.tierriapps.myworkoutorganizer.feature_main.domain.models.Reps
import com.tierriapps.myworkoutorganizer.feature_main.utils.ExerciseType

class CreateExercise {
    operator fun invoke(
        name: String,
        description: String,
        numOfSeries: Int,
        timeOfRest: Int,
        weight: Int,
        type: ExerciseType = ExerciseType.NORMAL,
        seriesDone: MutableList<Reps> = mutableListOf()
    ): Exercise {

    }
}