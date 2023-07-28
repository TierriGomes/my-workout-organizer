package com.tierriapps.myworkoutorganizer.feature_main.domain.usecases

import com.tierriapps.myworkoutorganizer.feature_main.domain.models.Division
import com.tierriapps.myworkoutorganizer.feature_main.domain.models.Exercise
import com.tierriapps.myworkoutorganizer.feature_main.utils.DivisionName
import com.tierriapps.myworkoutorganizer.feature_main.utils.ExerciseType
import org.junit.Assert.*

import org.junit.Before
import org.junit.Test

class PutExerciseInDivisionTest {

    // class under test
    private lateinit var putExerciseInDivision: PutExerciseInDivision

    @Before
    fun setUp() {
        putExerciseInDivision = PutExerciseInDivision()
    }

    @Test
    fun `putExerciseInDivision just add the exercise to the division`(){
        // GIVEN
        val exercises = mutableListOf(
            Exercise(
                "push ups", "test", 2,
                60, ExerciseType.DROP_SET, 999),
            Exercise(
                "pull ups", "test", 3,
                80, ExerciseType.NORMAL, 10)
        )
        var division = Division(DivisionName.A, "test")
        // WHEN
        val result1 = putExerciseInDivision.invoke(division, exercises[0])
        val result2 = putExerciseInDivision.invoke(division, exercises[1])
        division = putExerciseInDivision.invoke(division, exercises[0])
        division = putExerciseInDivision.invoke(division, exercises[1])
        // THEN
        assertEquals(1, result1.exercises.size)
        assertEquals(1, result2.exercises.size)
        assertEquals(2, division.exercises.size)
        assertEquals("pull ups", division.exercises[1].name)
    }
}