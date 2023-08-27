package com.tierriapps.myworkoutorganizer.feature_main.presenter.models

import com.tierriapps.myworkoutorganizer.common.utils.UiText
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