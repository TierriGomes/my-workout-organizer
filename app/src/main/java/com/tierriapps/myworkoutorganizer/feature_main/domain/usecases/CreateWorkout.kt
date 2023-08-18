package com.tierriapps.myworkoutorganizer.feature_main.domain.usecases

import com.tierriapps.myworkoutorganizer.R
import com.tierriapps.myworkoutorganizer.common.utils.Resource
import com.tierriapps.myworkoutorganizer.common.utils.UiText
import com.tierriapps.myworkoutorganizer.feature_main.domain.models.Division
import com.tierriapps.myworkoutorganizer.feature_main.domain.models.Workout

class CreateWorkout {
    operator fun invoke(
        name: String,
        description: String,
        divisions: List<Division?>,
    ): Resource<Workout?> {
        val n = name.trim()
        val d = description.trim()
        if (n == ""){
            return Resource.Error(null, UiText.StringResource(R.string.empty_name))
        }
        else if (n.length > 20){
            return Resource.Error(null, UiText.StringResource(R.string.too_long_name))
        }
        else if (d == ""){
            return Resource.Error(null, UiText.StringResource(R.string.empty_description))
        }
        else if (d.length > 100){
            return Resource.Error(null, UiText.StringResource(R.string.too_long_description))
        }
        else if (divisions.isEmpty()){
            return Resource.Error(null, UiText.StringResource(R.string.no_division_created))
        }
        else if (divisions.contains(null)){
            return Resource.Error(null, UiText.StringResource(R.string.invalid_divisions))
        }
        return Resource.Success(
            Workout(name = n, description = d, divisions = divisions as List<Division>),
            UiText.StringResource(R.string.workout_created)
        )
    }
}