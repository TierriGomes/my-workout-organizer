package com.tierriapps.myworkoutorganizer.feature_main.domain.usecases

import androidx.test.filters.SmallTest
import com.tierriapps.myworkoutorganizer.feature_main.domain.models.Division
import com.tierriapps.myworkoutorganizer.feature_main.domain.models.Exercise
import com.tierriapps.myworkoutorganizer.feature_main.domain.models.Reps
import com.tierriapps.myworkoutorganizer.feature_main.domain.models.Workout
import com.tierriapps.myworkoutorganizer.feature_main.utils.DivisionName
import com.tierriapps.myworkoutorganizer.feature_main.utils.ExerciseType
import org.junit.Assert.*

import org.junit.Before
import org.junit.Test

@SmallTest
class PutDivisionTrainedInWorkoutTest {

    // class under test
    private lateinit var putDivisionTrainedInWorkout: PutDivisionTrainedInWorkout

    @Before
    fun setUp() {
        putDivisionTrainedInWorkout = PutDivisionTrainedInWorkout()
    }

    @Test
    fun `putDivisionTrainedInWorkout add the division in divisionsDone if its registered in divisions`(){
        // GIVEN
        val exercises = mutableListOf(
            Exercise(
                "push ups", "test", 2,
                60, ExerciseType.DROP_SET, 999),
            Exercise(
                "pull ups", "test", 3,
                80, ExerciseType.NORMAL, 10)
        )
        val exercisesDone = mutableListOf(
            Exercise(
                "push ups", "test", 2,
                60, ExerciseType.DROP_SET, 999, mutableListOf(Reps(listOf(1, 2, 3)))
            ),
            Exercise(
                "pull ups", "test", 3,
                80, ExerciseType.NORMAL, 10, mutableListOf(Reps(listOf(4, 5, 6)))
            )
        )
        val division = Division(DivisionName.D, "test", exercises)
        val divisionDone = Division(DivisionName.D, "test", exercisesDone)
        var workout = Workout(name = "my Workout", divisions = listOf(division))
        // WHEN
        val trainingsDoneSize = workout.trainingsDone.size
        workout = putDivisionTrainedInWorkout(workout, divisionDone)
        // THEN
        assertEquals(trainingsDoneSize+1, workout.trainingsDone.size)
        assertEquals(exercisesDone, workout.trainingsDone[0].exercises)
    }

    @Test
    fun `putDivisionTrainedInWorkout don't add the division in divisionsDone if its not registered in divisions`(){
        // GIVEN
        val exercises = mutableListOf(
            Exercise(
                "push ups", "test", 2,
                60, ExerciseType.DROP_SET, 999),
            Exercise(
                "pull ups", "test", 3,
                80, ExerciseType.NORMAL, 10)
        )
        val exercisesDone = mutableListOf(
            Exercise(
                "different", "test", 2,
                60, ExerciseType.DROP_SET, 999, mutableListOf(Reps(listOf(1, 2, 3)))
            ),
            Exercise(
                "pull ups", "test", 3,
                80, ExerciseType.NORMAL, 10, mutableListOf(Reps(listOf(4, 5, 6)))
            )
        )
        val division = Division(DivisionName.D, "test", exercises)
        val divisionDone = Division(DivisionName.D, "test", exercisesDone)
        var workout = Workout(name = "my Workout", divisions = listOf(division))
        // WHEN
        workout = putDivisionTrainedInWorkout(workout, divisionDone)
        // THEN
        assertTrue(workout.trainingsDone.isEmpty())

    }
}