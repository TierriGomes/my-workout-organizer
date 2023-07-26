package com.tierriapps.myworkoutorganizer.feature_main.domain.usecases

import com.tierriapps.myworkoutorganizer.R
import com.tierriapps.myworkoutorganizer.common.utils.Resource
import com.tierriapps.myworkoutorganizer.feature_main.domain.models.Exercise
import com.tierriapps.myworkoutorganizer.feature_main.utils.DivisionName
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class CreateDivisionTest {

    // class under test
    private lateinit var createDivision: CreateDivision

    @Before
    fun setUp(){
        createDivision = CreateDivision()
    }



    @Test
    fun `createDivision with a empty description returns a ResourceError`(){
        // GIVEN
        val name = DivisionName.A
        val description = ""
        // WHEN
        val result = createDivision.invoke(name, description)
        // THEN
        assertTrue(result is Resource.Error)
        assertEquals(R.string.empty_description, result.message?.resId())
    }


    @Test
    fun `createDivision giving valid name and description returns a ResourceSuccess with the division`(){
        // GIVEN
        val name = DivisionName.A
        val description = "valid"
        // WHEN
        val result = createDivision.invoke(name, description)
        // THEN
        assertTrue(result is Resource.Success)
        assertEquals("valid", result.content?.name)
    }

    @Test
    fun `createDivision giving valid inputs and a listOfExercises returns the division with the given list`(){
        // GIVEN
        val name = DivisionName.A
        val description = "valid"
        val listOfExercises = listOf(Exercise("test"))
        // WHEN
        val result = createDivision.invoke(name, description, listOfExercises)
        // THEN
        assertTrue(result is Resource.Success)
        assertEquals("test", result.content?.exercises?.get(0)?.name)
    }

}