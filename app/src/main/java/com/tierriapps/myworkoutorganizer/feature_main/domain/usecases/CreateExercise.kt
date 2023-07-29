package com.tierriapps.myworkoutorganizer.feature_main.domain.usecases

import com.tierriapps.myworkoutorganizer.R
import com.tierriapps.myworkoutorganizer.common.utils.Resource
import com.tierriapps.myworkoutorganizer.common.utils.UiText
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
    ): Resource<Exercise?> {
        if(name == ""){
            return Resource.Error(null, UiText.StringResource(R.string.empty_name))
        }
        else if (name.trim().length > 20) {
            return Resource.Error(null, UiText.StringResource(R.string.too_long_name))
        }
        else if (description.trim().length > 100) {
            return Resource.Error(null, UiText.StringResource(R.string.too_long_description))
        }
        else if (numOfSeries !in 1..6) {
            return Resource.Error(null, UiText.StringResource(R.string.invalid_num_of_series))
        }
        else if (timeOfRest !in 0..999){
            return Resource.Error(null, UiText.StringResource(R.string.invalid_time_of_rest))
        }
        else if (weight !in 0..999) {
            return Resource.Error(null, UiText.StringResource(R.string.invalid_weight))
        }
        val n = name.trim()
        val d = if (description.trim() != "") description.trim() else n
        var mySeriesDone = seriesDone.toMutableList()
        if (mySeriesDone.size > numOfSeries) {
            mySeriesDone = mySeriesDone.filter { mySeriesDone.indexOf(it) < numOfSeries }.toMutableList()
            return Resource.Error(
                Exercise(n, d, numOfSeries, timeOfRest, type, weight, mySeriesDone),
                UiText.StringDynamic("Invalid operation error"))
        }
        return Resource.Success(
            Exercise(n, d, numOfSeries, timeOfRest, type, weight, seriesDone),
            UiText.StringResource(R.string.exercise_created)
        )
    }
}