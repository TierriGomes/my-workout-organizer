package com.tierriapps.myworkoutorganizer.feature_main.domain.usecases

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.filters.SmallTest
import com.tierriapps.myworkoutorganizer.feature_main.domain.models.Workout
import com.tierriapps.myworkoutorganizer.feature_main.domain.repositories.WorkoutRepository
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*

import org.junit.Before
import org.junit.Rule
import org.junit.Test

@SmallTest
@ExperimentalCoroutinesApi
class SaveWorkoutTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    // class under test
    private lateinit var saveWorkout: SaveWorkout
    private var mockRepository: WorkoutRepository = mockk(relaxed = true)
    @Before
    fun setUp() {
        saveWorkout = SaveWorkout(mockRepository)
    }

    @Test
    fun `saveWorkout just call the repository`() = runTest {
        // GIVEN
        val workout = Workout(1, "name")
        // WHEN
        saveWorkout.invoke(workout)
        advanceUntilIdle()
        // THEN
        coVerify {
            mockRepository.insertWorkout(workout)
        }
    }
}