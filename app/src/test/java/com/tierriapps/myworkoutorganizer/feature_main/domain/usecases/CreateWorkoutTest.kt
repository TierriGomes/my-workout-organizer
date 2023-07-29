package com.tierriapps.myworkoutorganizer.feature_main.domain.usecases

import androidx.test.filters.SmallTest
import com.tierriapps.myworkoutorganizer.R
import com.tierriapps.myworkoutorganizer.common.utils.Resource
import com.tierriapps.myworkoutorganizer.feature_main.domain.models.Division
import com.tierriapps.myworkoutorganizer.feature_main.utils.DivisionName
import org.junit.Assert.*

import org.junit.Before
import org.junit.Test

@SmallTest
class CreateWorkoutTest {

    // class under teste
    private lateinit var createWorkout: CreateWorkout

    @Before
    fun setUp() {
        createWorkout = CreateWorkout()
    }

    @Test
    fun `createWorkout when name is empty return a ResourceError`(){
        // GIVEN
        val name = ""
        val description = "description"
        val listOfDivisions = listOf<Division>(Division(DivisionName.A, ""))
        // WHEN
        val result = createWorkout(name, description, listOfDivisions)
        // THEN
        assertTrue(result is Resource.Error)
        assertEquals(R.string.empty_name, result.message?.resId())
    }

    @Test
    fun `createWorkout when name is too big return a ResourceError`(){
        // GIVEN
        val name = "name name name name name name name name name name name name name name name name"
        val description = "description"
        val listOfDivisions = listOf<Division>(Division(DivisionName.A, ""))
        // WHEN
        val result = createWorkout(name, description, listOfDivisions)
        // THEN
        assertTrue(result is Resource.Error)
        assertEquals(R.string.too_long_name, result.message?.resId())
    }

    @Test
    fun `createWorkout when description is empty return a ResourceError`(){
        // GIVEN
        val name = "valid name"
        val description = ""
        val listOfDivisions = listOf<Division>(Division(DivisionName.A, ""))
        // WHEN
        val result = createWorkout(name, description, listOfDivisions)
        // THEN
        assertTrue(result is Resource.Error)
        assertEquals(R.string.empty_description, result.message?.resId())
    }

    @Test
    fun `createWorkout when description is too big return a ResourceError`(){
        // GIVEN
        val name = "valid"
        val description = "description description description description description description" +
                "description description description description description description description" +
                "description description description description description description description" +
                "description description description description description description description" +
                "description description description description description description description"
        val listOfDivisions = listOf<Division>(Division(DivisionName.A, ""))
        // WHEN
        val result = createWorkout(name, description, listOfDivisions)
        // THEN
        assertTrue(result is Resource.Error)
        assertEquals(R.string.too_long_description, result.message?.resId())
    }

    @Test
    fun `createWorkout when listOfDivisions is empty return a ResourceError`(){
        // GIVEN
        val name = "valid"
        val description = "description"
        val listOfDivisions = listOf<Division>()
        // WHEN
        val result = createWorkout(name, description, listOfDivisions)
        // THEN
        assertTrue(result is Resource.Error)
        assertEquals(R.string.no_division_created, result.message?.resId())
    }

    @Test
    fun `createWorkout when all fields is valid return a ResourceSuccess`(){
        // GIVEN
        val name = "name"
        val description = "description"
        val listOfDivisions = listOf<Division>(Division(DivisionName.A, ""))
        // WHEN
        val result = createWorkout(name, description, listOfDivisions)
        // THEN
        assertTrue(result is Resource.Success)
        assertTrue(result.content != null)
    }
}