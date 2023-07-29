package com.tierriapps.myworkoutorganizer.feature_main.domain.usecases

import com.tierriapps.myworkoutorganizer.feature_main.domain.models.Division
import com.tierriapps.myworkoutorganizer.feature_main.domain.models.Exercise

class PutExerciseInDivision {
    operator fun invoke(division: Division, exercise: Exercise): Division {
        val myDivision = Division(
            division.name,
            division.description,
            division.exercises.toMutableList()
        )
        myDivision.exercises.add(exercise)
        return myDivision
    }
}