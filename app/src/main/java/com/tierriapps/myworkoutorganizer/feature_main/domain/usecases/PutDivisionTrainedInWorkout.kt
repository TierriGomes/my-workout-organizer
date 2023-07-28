package com.tierriapps.myworkoutorganizer.feature_main.domain.usecases

import com.tierriapps.myworkoutorganizer.feature_main.domain.models.Division
import com.tierriapps.myworkoutorganizer.feature_main.domain.models.Reps
import com.tierriapps.myworkoutorganizer.feature_main.domain.models.Workout

class PutDivisionTrainedInWorkout {
    operator fun invoke(workout: Workout, division: Division): Workout {
        var isValidDivision = false
        for(div in workout.divisions){
            if (div.name == division.name) {
                for((key, exercice) in div.exercises.withIndex()){
                    if(
                        exercice.name == division.exercises[key].name
                        && exercice.description == division.exercises[key].description
                        && exercice.numOfSeries == division.exercises[key].numOfSeries
                        && exercice.type == division.exercises[key].type
                        && exercice.timeOfRest == division.exercises[key].timeOfRest
                    ){
                        isValidDivision = true
                    }
                    while( division.exercises[key].seriesDone.size < exercice.numOfSeries){
                        division.exercises[key].seriesDone.add(Reps(listOf(0)))
                    }
                }
            }
            if (isValidDivision){
                workout.trainingsDone.add(division)
                return workout
            }
        }
        return workout
    }
}