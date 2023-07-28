package com.tierriapps.myworkoutorganizer.feature_main.domain.usecases

import com.tierriapps.myworkoutorganizer.feature_main.domain.models.Exercise
import com.tierriapps.myworkoutorganizer.feature_main.domain.models.Reps

class PutRepsInOneSeriesOfAnExercise {
    operator fun invoke(exercise: Exercise, reps: List<Int>): Exercise {
        val reps = Reps(reps)
        if (exercise.seriesDone.size < exercise.numOfSeries){
            exercise.seriesDone.add(reps)
            return exercise
        }else {
            return exercise
        }
    }
}