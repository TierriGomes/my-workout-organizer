package com.tierriapps.myworkoutorganizer.feature_main.presenter.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tierriapps.myworkoutorganizer.feature_main.data.repositories.FakeRepository
import com.tierriapps.myworkoutorganizer.feature_main.domain.usecases.GetActualWorkout
import com.tierriapps.myworkoutorganizer.feature_main.utils.DivisionName
import com.tierriapps.myworkoutorganizer.testutils.getOrAwaitValue
import com.tierriapps.myworkoutorganizer.testutils.getOrAwaitValues
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.*

import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class MainViewModelTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()
    private val dispatcher = StandardTestDispatcher()

    // class under test
    private lateinit var viewModel: MainViewModel
    private lateinit var fakeRepository: FakeRepository
    @Before
    fun setUp(){
        Dispatchers.setMain(dispatcher)
        fakeRepository = FakeRepository()
        fakeRepository.shouldReturnSuccess = true
        val getActualWorkout = GetActualWorkout(fakeRepository)
        viewModel = MainViewModel(getActualWorkout)
    }

    @After
    fun tearDown(){
        Dispatchers.resetMain()
    }
    @Test
    fun `getActualWorkoutAndSetValues will call the useCase get the actual workout`()
    = runTest {
        // given
        val workout = fakeRepository.getWorkoutById(1).last().content!!

        // when, then
        viewModel.actualWorkout.getOrAwaitValues(
            duringObserve = {
                println(it.content)
            },
            afterObserve = {
                viewModel.getActualWorkoutAndSetValues()
                advanceUntilIdle()
                assertEquals(workout, it.last().content!!)
            }
        )
    }

    @Test
    fun `getActualWorkoutAndSetValues will call the useCase and set the DivisionForm`()
            = runTest {
        // given
        val workout = fakeRepository.getWorkoutById(1).last().content!!

        viewModel.divisionsForm.getOrAwaitValues(
            duringObserve = {
                println(it.toString())
            },
            afterObserve = {
                viewModel.getActualWorkoutAndSetValues()
                advanceUntilIdle()
                val result = it.last()
                assertEquals(2, result.size)
                for (division in result) {
                    for (exercise in division.exercises) {
                        println(exercise.name)
                        assertTrue(exercise.numOfSeries != 0 && exercise.numOfSeries != null)
                        assertEquals(0, exercise.repsDone.size)
                    }
                }
            }
        )
    }

    @Test
    fun `getActualWorkoutAndSetValues will call the useCase and set the ActualDivisions`()
            = runTest {
        // given
        val workout = fakeRepository.getWorkoutById(1).last().content!!

        viewModel.actualTrainings.getOrAwaitValues(
            duringObserve = {
                println(it.toString())
            },
            afterObserve = {
                viewModel.getActualWorkoutAndSetValues()
                advanceUntilIdle()
                val result = it.last()
                assertEquals(2, result.size)
                for ((key, division) in result.withIndex()) {
                    for (exercise in division.exercises) {
                        println(exercise.name)
                        assertTrue(exercise.numOfSeries != 0 && exercise.numOfSeries != null)
                        println(exercise.repsDone.toString())
                        if (key < 1){
                            assertTrue(exercise.repsDone.isEmpty())
                        }else {
                            assertTrue(exercise.repsDone.isNotEmpty())
                        }
                    }
                }
            }
        )
    }

    @Test
    fun `selectDivision will change the the divison`()
            = runTest {
        // given
        val workout = fakeRepository.getWorkoutById(1).last().content!!

        val divisionToSelect = viewModel.divisionsForm.getOrAwaitValue{
            viewModel.getActualWorkoutAndSetValues()
            advanceUntilIdle()
        }?.get(0)!!

        viewModel.actualTrainings.getOrAwaitValues(
            duringObserve = {
                println(it.toString())
            },
            afterObserve = {
                viewModel.selectDivision(divisionToSelect)
                advanceUntilIdle()
                val first = it[0]
                val result = it.last()
                assertEquals(2, result.size)
                assertEquals('B', first[0].name)
                assertEquals('A', result[0].name)
                for ((key, division) in result.withIndex()) {
                    for (exercise in division.exercises) {
                        println(exercise.name)
                        assertTrue(exercise.numOfSeries != 0 && exercise.numOfSeries != null)
                        println(exercise.repsDone.toString())
                        if (key < 1){
                            assertTrue(exercise.repsDone.isEmpty())
                        }else {
                            assertTrue(exercise.repsDone.isNotEmpty())
                        }
                    }
                }
                for ((key, division) in first.withIndex()) {
                    for (exercise in division.exercises) {
                        println(exercise.name)
                        assertTrue(exercise.numOfSeries != 0 && exercise.numOfSeries != null)
                        println(exercise.repsDone.toString())
                        if (key < 1){
                            assertTrue(exercise.repsDone.isEmpty())
                        }else {
                            assertTrue(exercise.repsDone.isNotEmpty())
                        }
                    }
                }
            }
        )
    }
}