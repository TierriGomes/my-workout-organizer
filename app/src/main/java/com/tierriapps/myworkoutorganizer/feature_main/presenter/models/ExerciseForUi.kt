package com.tierriapps.myworkoutorganizer.feature_main.presenter.models

import com.tierriapps.myworkoutorganizer.common.utils.UiText
import com.tierriapps.myworkoutorganizer.feature_main.domain.models.Exercise
import com.tierriapps.myworkoutorganizer.feature_main.utils.ExerciseType

data class ExerciseForUi (
    var name: String? = null,
    var description: String? = null,
    var numOfSeries: Int? = null,
    var timeOfRest: Int? = null,
    var type: ExerciseType = ExerciseType.NORMAL,
    var weight: Int? = null,
    var image: String? = null,
    var status: Boolean = true,
    var message: UiText? = null,
    var repsDone: MutableList<MutableList<Int>> = mutableListOf()
)

fun Exercise.toExerciseForUi(): ExerciseForUi{
    return ExerciseForUi(
        name = this.name,
        description = this.description,
        numOfSeries = this.numOfSeries,
        timeOfRest = this.timeOfRest,
        type = this.type,
        weight = this.weight,
        image = this.image,
        status = true,
        message = null,
        repsDone = this.seriesDone.map {
            it.reps.toMutableList()
        }.toMutableList()
    )
}