package com.tierriapps.myworkoutorganizer.feature_main.domain.models

import com.tierriapps.myworkoutorganizer.feature_main.utils.ExerciseType

data class Exercise(
    val name: String = "",
    val description: String = "",
    val numOfSeries: Int = 0,
    val timeOfRest: Int = 0,
    val type: ExerciseType = ExerciseType.NORMAL,
    val weight: Int = 0,
    val seriesDone: MutableList<Reps> = mutableListOf(),
    val image: String = ""
)

