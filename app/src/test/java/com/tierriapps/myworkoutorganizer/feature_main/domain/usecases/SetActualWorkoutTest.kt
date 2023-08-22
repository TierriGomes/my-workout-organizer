package com.tierriapps.myworkoutorganizer.feature_main.domain.usecases

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.filters.SmallTest
import com.tierriapps.myworkoutorganizer.feature_main.data.repositories.FakeRepository
import com.tierriapps.myworkoutorganizer.feature_main.domain.models.Workout
import io.mockk.coVerify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*

import org.junit.Before
import org.junit.Rule
import org.junit.Test

@SmallTest
@ExperimentalCoroutinesApi
class SetActualWorkoutTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    // class under test
    private lateinit var setActualWorkout: SetActualWorkout
    private lateinit var fakeRepository: FakeRepository

    @Before
    fun setUp() {
        fakeRepository = FakeRepository()
        setActualWorkout = SetActualWorkout(fakeRepository)
    }

    @Test
    fun `setActualWorkout call repository to set id if the workout is already saved`() = runTest {
        // GIVEN
        fakeRepository.shouldReturnSuccess = true
        val workout = Workout(99, "test")
        // WHEN
        setActualWorkout.invoke(workout).toList()
        advanceUntilIdle()
        // THEN
        assertEquals(1, fakeRepository.getWorkoutByIdCalls)
        assertEquals(1, fakeRepository.setActualWorkoutCalls)
    }

    @Test
    fun `setActualWorkout don't call repository to set id if the workout is not saved`() = runTest {
        // GIVEN
        fakeRepository.shouldReturnSuccess = false
        val workout = Workout(99, "test")
        // WHEN
        setActualWorkout.invoke(workout).toList()
        advanceUntilIdle()
        // THEN
        assertEquals(1, fakeRepository.getWorkoutByIdCalls)
        assertEquals(0, fakeRepository.setActualWorkoutCalls)
    }

    @Test
    fun `setActualWorkout emits just one result`() = runTest {
        fakeRepository.shouldReturnSuccess = true
        val workout = Workout(null, "name2", "description")

        val flow = setActualWorkout.invoke(workout).toList()
        println(flow.toString())

    }
}