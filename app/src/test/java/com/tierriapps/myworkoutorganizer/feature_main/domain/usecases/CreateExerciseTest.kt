package com.tierriapps.myworkoutorganizer.feature_main.domain.usecases

import com.tierriapps.myworkoutorganizer.R
import com.tierriapps.myworkoutorganizer.common.utils.Resource
import com.tierriapps.myworkoutorganizer.feature_main.domain.models.Reps
import com.tierriapps.myworkoutorganizer.feature_main.utils.ExerciseType
import org.junit.Assert.*

import org.junit.Before
import org.junit.Test

class CreateExerciseTest {

    // class under test
    private lateinit var createExercise: CreateExercise

    @Before
    fun setUp() {
        createExercise = CreateExercise()
    }

    @Test
    fun `createExercise when name is empty return a ResourceError`(){
        // GIVEN
        val name = ""
        val description = "valid"
        val numOfSeries = 1
        val timeOfRest: Int = 1
        val type: ExerciseType = ExerciseType.NORMAL
        val weight: Int = 1
        val seriesDone: MutableList<Reps> = mutableListOf()
        // WHEN
        val result = createExercise(
            name, description, numOfSeries, timeOfRest,
            weight = weight, type = type, seriesDone
        )
        // THEN
        assertTrue(result is Resource.Error)
        assertEquals(R.string.empty_name, result.message?.resId())
    }

    @Test
    fun `createExercise when name is too big return a ResourceError`(){
        // GIVEN
        val name: String = "some name bigger than twenty characters"
        val description: String = "valid"
        val numOfSeries: Int = 1
        val timeOfRest: Int = 1
        val type: ExerciseType = ExerciseType.NORMAL
        val weight: Int = 1
        val seriesDone: MutableList<Reps> = mutableListOf()
        // WHEN
        val result = createExercise(
            name, description, numOfSeries, timeOfRest,
            weight = weight, type = type, seriesDone
        )
        // THEN
        assertTrue(result is Resource.Error)
        assertEquals(R.string.too_long_name, result.message?.resId())
    }

    @Test
    fun `createExercise when description is empty set description as the name and returns a ResourceSuccess`(){
        // GIVEN
        val name: String = "test"
        val description: String = ""
        val numOfSeries: Int = 1
        val timeOfRest: Int = 1
        val type: ExerciseType = ExerciseType.NORMAL
        val weight: Int = 1
        val seriesDone: MutableList<Reps> = mutableListOf()
        // WHEN
        val result = createExercise(
            name, description, numOfSeries, timeOfRest,
            weight = weight, type = type, seriesDone
        )
        // THEN
        assertTrue(result is Resource.Success)
        assertEquals(result.content?.name, result.content?.description)
    }

    @Test
    fun `createExercise when description is too big returns a ResourceError`(){
        // GIVEN
        val name: String = "test"
        val description: String = "some very big description... " +
                "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" +
                "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" +
                "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" +
                "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" +
                "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
        val numOfSeries: Int = 1
        val timeOfRest: Int = 1
        val type: ExerciseType = ExerciseType.NORMAL
        val weight: Int = 1
        val seriesDone: MutableList<Reps> = mutableListOf()
        // WHEN
        val result = createExercise(
            name, description, numOfSeries, timeOfRest,
            weight = weight, type = type, seriesDone
        )
        // THEN
        assertTrue(result is Resource.Error)
        assertEquals(R.string.too_long_description, result.message?.resId())
    }

    @Test
    fun `createExercise when numOfSeries is not between 1 and 6 returns a ResourceError`(){
        // GIVEN
        val name: String = "test"
        val description: String = ""
        val numOfSeries: Int = 7
        val timeOfRest: Int = 1
        val type: ExerciseType = ExerciseType.NORMAL
        val weight: Int = 1
        val seriesDone: MutableList<Reps> = mutableListOf()
        // WHEN
        val result = createExercise(
            name, description, numOfSeries, timeOfRest,
            weight = weight, type = type, seriesDone
        )
        // THEN
        assertTrue(result is Resource.Error)
        assertEquals(R.string.invalid_num_of_series, result.message?.resId())
    }

    @Test
    fun `createExercise when timeOfRest is not between 0 and 999 set it as 60 and returns a ResourceError`(){
        // GIVEN
        val name: String = "test"
        val description: String = ""
        val numOfSeries: Int = 1
        val timeOfRest: Int = 1000
        val type: ExerciseType = ExerciseType.NORMAL
        val weight: Int = 1
        val seriesDone: MutableList<Reps> = mutableListOf()
        // WHEN
        val result = createExercise(
            name, description, numOfSeries, timeOfRest,
            weight = weight, type = type, seriesDone
        )
        // THEN
        assertTrue(result is Resource.Error)
        assertEquals(R.string.invalid_time_of_rest, result.message?.resId())
    }

    @Test
    fun `createExercise when seriesDone is bigger than numOfSeries will take just the first values and returns a ResourceError`(){
        // GIVEN
        val name: String = "test"
        val description: String = ""
        val numOfSeries: Int = 1
        val timeOfRest: Int = 0
        val type: ExerciseType = ExerciseType.NORMAL
        val weight: Int = 1
        val seriesDone: MutableList<Reps> = mutableListOf(
            Reps(listOf(1, 2)), Reps(listOf(2, 3))
        )
        // WHEN
        val result = createExercise(
            name, description, numOfSeries, timeOfRest,
            weight = weight, type = type, seriesDone
        )
        // THEN
        assertTrue(result is Resource.Error)
        assertEquals(1, result.content?.seriesDone?.size)
        assertEquals(listOf(1, 2), result.content?.seriesDone?.get(0)?.reps)
        assertEquals("Invalid operation error", result.message?.asString())
    }

    @Test
    fun `createExercise when weight is not between 0 and 999 set it as 0 and returns a ResourceError`(){
        // GIVEN
        val name: String = "test"
        val description: String = ""
        val numOfSeries: Int = 1
        val timeOfRest: Int = 1000
        val type: ExerciseType = ExerciseType.NORMAL
        val weight: Int = 1000
        val seriesDone: MutableList<Reps> = mutableListOf()
        // WHEN
        val result = createExercise(
            name, description, numOfSeries, timeOfRest,
            weight = weight, type = type, seriesDone
        )
        // THEN
        assertTrue(result is Resource.Error)
        assertEquals(R.string.invalid_weight, result.message?.resId())
    }



}