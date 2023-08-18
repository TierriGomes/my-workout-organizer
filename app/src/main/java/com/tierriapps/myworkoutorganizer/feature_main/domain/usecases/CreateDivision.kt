package com.tierriapps.myworkoutorganizer.feature_main.domain.usecases

import com.tierriapps.myworkoutorganizer.R
import com.tierriapps.myworkoutorganizer.common.utils.Resource
import com.tierriapps.myworkoutorganizer.common.utils.UiText
import com.tierriapps.myworkoutorganizer.feature_main.domain.models.Division
import com.tierriapps.myworkoutorganizer.feature_main.domain.models.Exercise
import com.tierriapps.myworkoutorganizer.feature_main.utils.DivisionName

class CreateDivision {
    operator fun invoke(
        name: DivisionName,
        description: String,
        listOfExercises: List<Exercise?> = listOf()
    ): Resource<Division?> {
        if (description.trim() == ""){
            return Resource.Error(null, UiText.StringResource(R.string.empty_description))
        }
        if (null in listOfExercises || listOfExercises.isEmpty()){
            return Resource.Error(null, UiText.StringResource(R.string.invalid_exercises))
        }
        return Resource.Success(
            Division(name, description.trim(), listOfExercises as MutableList<Exercise>),
            UiText.StringDynamic("")
        )
    }
}