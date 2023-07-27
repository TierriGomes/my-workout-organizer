package com.tierriapps.myworkoutorganizer.feature_main.domain.usecases

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.*

import org.junit.Before
import org.junit.Rule

@ExperimentalCoroutinesApi
class GetActualWorkoutTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    // class underTest
    private lateinit var getActualWorkout: GetActualWorkout

    @Before
    fun setUp() {
    }
}