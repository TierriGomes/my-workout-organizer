package com.tierriapps.myworkoutorganizer.feature_main.domain.usecases

import androidx.test.filters.SmallTest
import com.tierriapps.myworkoutorganizer.feature_main.domain.models.Exercise
import com.tierriapps.myworkoutorganizer.feature_main.domain.models.Reps
import com.tierriapps.myworkoutorganizer.feature_main.utils.ExerciseType
import org.junit.Assert.*

import org.junit.Before
import org.junit.Test

@SmallTest
class PutRepsInOneSeriesOfAnExerciseTest {

    // class under test
    private lateinit var putRepsInOneSeriesOfAnExercise: PutRepsInOneSeriesOfAnExercise

    @Before
    fun setUp() {
        putRepsInOneSeriesOfAnExercise = PutRepsInOneSeriesOfAnExercise()
    }

    @Test
    fun `putRepsInOneSeries just add the reps to de reps list if it has space`(){
        // GIVEN
        val reps = listOf(12,10,8)
        val exercise = Exercise(
            "push ups", "test", 2,
            60, ExerciseType.DROP_SET, 999)
        // WHEN
        val result = putRepsInOneSeriesOfAnExercise.invoke(exercise, reps)
        // THEN
        assertEquals(1, result.seriesDone.size)
    }

    @Test
    fun `putRepsInOneSeries don't add the reps to de reps list if it has no space`(){
        // GIVEN
        val reps = listOf(12,10,8)
        val exercise = Exercise(
            "push ups", "test", 2,
            60, ExerciseType.DROP_SET, 999,
            seriesDone = mutableListOf(Reps(listOf(1)), Reps(listOf(2)))
        )
        // WHEN
        val result = putRepsInOneSeriesOfAnExercise.invoke(exercise, reps)
        // THEN
        assertEquals(2, result.seriesDone.size)
        assertEquals(exercise.seriesDone, result.seriesDone)
    }
}