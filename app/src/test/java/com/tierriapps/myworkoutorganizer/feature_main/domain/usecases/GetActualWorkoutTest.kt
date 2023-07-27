package com.tierriapps.myworkoutorganizer.feature_main.domain.usecases

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tierriapps.myworkoutorganizer.common.utils.Resource
import com.tierriapps.myworkoutorganizer.feature_main.data.repositories.FakeRepository
import io.mockk.coVerify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*

import org.junit.Before
import org.junit.Rule
import org.junit.Test

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
        coVerify { fakeRepository.getActualWorkoutId() }
        coVerify(inverse = true) { fakeRepository.getWorkoutById(any()) }
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
        coVerify { fakeRepository.getActualWorkoutId() }
        coVerify{ fakeRepository.getWorkoutById(1) }
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
        coVerify { fakeRepository.getActualWorkoutId() }
        coVerify{ fakeRepository.getWorkoutById(1) }
        assertEquals(2, result.size)
        assertTrue(result[0] is Resource.Loading)
        assertTrue(result[1] is Resource.Loading)
    }
}