package com.tierriapps.myworkoutorganizer.feature_main.domain.usecases

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tierriapps.myworkoutorganizer.common.utils.Resource
import com.tierriapps.myworkoutorganizer.feature_main.data.repositories.FakeRepository
import io.mockk.coVerify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*

import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class GetAllWorkoutsTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    // class under test
    private lateinit var getAllWorkouts: GetAllWorkouts
    private lateinit var fakeRepository: FakeRepository

    @Before
    fun setUp() {
        fakeRepository = FakeRepository()
        getAllWorkouts = GetAllWorkouts(fakeRepository)
    }

    @Test
    fun `getAllWorkouts call repository and emit his ResourceErrors`()
    = runTest {
        // GIVEN
        fakeRepository.shouldReturnSuccess = false
        // WHEN
        val result = getAllWorkouts.invoke().toList()
        // THEN
        coVerify { fakeRepository.getAllWorkouts() }
        assertEquals(2, result.size)
        assertTrue(result[0] is Resource.Loading)
        assertTrue(result[1] is Resource.Error)
    }

    @Test
    fun `getAllWorkouts call repository and emit his ResourceSuccess`()
    = runTest {
        // GIVEN
        fakeRepository.shouldReturnSuccess = true
        // WHEN
        val result = getAllWorkouts.invoke().toList()
        // THEN
        coVerify { fakeRepository.getAllWorkouts() }
        assertEquals(2, result.size)
        assertTrue(result[0] is Resource.Loading)
        assertTrue(result[1] is Resource.Success)
    }
}