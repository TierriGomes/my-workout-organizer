package com.tierriapps.myworkoutorganizer.feature_main.domain.usecases

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.filters.SmallTest
import com.tierriapps.myworkoutorganizer.common.utils.Resource
import com.tierriapps.myworkoutorganizer.feature_main.data.repositories.FakeRepository
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*

import org.junit.Before
import org.junit.Rule
import org.junit.Test

@SmallTest
@ExperimentalCoroutinesApi
class GetActualWorkoutTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    // class underTest
    private lateinit var getActualWorkout: GetActualWorkout

    private lateinit var fakeRepository: FakeRepository


    @Before
    fun setUp() {
        fakeRepository = FakeRepository()
        getActualWorkout = GetActualWorkout(fakeRepository)
    }

    @Test
    fun `getActualWorkout when there is no actual id saved will return a ResourceError`()
    = runTest {
        // GIVEN
        fakeRepository.shouldReturnSuccess = false
        // WHEN
        val result = getActualWorkout.invoke().toList()
        advanceUntilIdle()
        // THEN
        assertEquals(1, fakeRepository.getActualWorkoutCalls)
        assertEquals(0, fakeRepository.getWorkoutByIdCalls)
        assertEquals(2, result.size)
        assertTrue(result[0] is Resource.Loading)
        assertTrue(result[1] is Resource.Error)
    }

    @Test
    fun `getActualWorkout when there is an actual id but not a workout, will return a ResourceError from repository`()
    = runTest {
        // GIVEN
        fakeRepository.shouldReturnSuccess = true
        fakeRepository.listOfWorkouts = listOf()
        // WHEN
        val result = getActualWorkout.invoke().toList()
        advanceUntilIdle()
        // THEN
        assertEquals(1, fakeRepository.getActualWorkoutCalls)
        assertEquals(1, fakeRepository.getWorkoutByIdCalls)
        assertEquals(2, result.size)
        assertTrue(result[0] is Resource.Loading)
        assertTrue(result[1] is Resource.Error)
    }

    @Test
    fun `getActualWorkout when there is an actual id and a workout, will return a ResourceSuccess from repository`()
    = runTest {
        // GIVEN
        fakeRepository.shouldReturnSuccess = true
        // WHEN
        val result = getActualWorkout.invoke().toList()
        advanceUntilIdle()
        // THEN
        assertEquals(1, fakeRepository.getActualWorkoutCalls)
        assertEquals(1, fakeRepository.getWorkoutByIdCalls)
        assertEquals(2, result.size)
        assertTrue(result[0] is Resource.Loading)
        assertTrue(result[1] is Resource.Success)
    }
}