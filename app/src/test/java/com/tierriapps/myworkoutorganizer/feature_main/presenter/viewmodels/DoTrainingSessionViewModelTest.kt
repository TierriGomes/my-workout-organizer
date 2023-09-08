package com.tierriapps.myworkoutorganizer.feature_main.presenter.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tierriapps.myworkoutorganizer.feature_main.data.repositories.FakeRepository
import com.tierriapps.myworkoutorganizer.feature_main.domain.usecases.GetActualWorkout
import com.tierriapps.myworkoutorganizer.feature_main.domain.usecases.PutDivisionTrainedInWorkout
import com.tierriapps.myworkoutorganizer.feature_main.domain.usecases.SaveWorkout
import com.tierriapps.myworkoutorganizer.testutils.getOrAwaitValues
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
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
class DoTrainingSessionViewModelTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()
    private val dispatcher = StandardTestDispatcher()

    // class under test
    private lateinit var viewModel: DoTrainingSessionViewModel
    private lateinit var fakeRepository: FakeRepository

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
        fakeRepository = FakeRepository()
        fakeRepository.shouldReturnSuccess = true
        val getActualWorkout = GetActualWorkout(fakeRepository)
        val putDivisionTrainedInWorkout = PutDivisionTrainedInWorkout()
        val saveWorkout = SaveWorkout(fakeRepository)
        viewModel =
            DoTrainingSessionViewModel(getActualWorkout, putDivisionTrainedInWorkout, saveWorkout)
    }

    @After
    fun tearDown() {
        dispatcher.cancel()
        Dispatchers.resetMain()
    }

    @Test
    fun `getActualDivisionTodo should initialize data`() = runTest {
        viewModel.divisionStatus.getOrAwaitValues {
            viewModel.getActualDivisionToDo("A")
            advanceUntilIdle()
            println(it.last()!!.exercises.toString())
            assertEquals('A', it.last()!!.name)
            for (exercise in it.last()!!.exercises){
                assertTrue(exercise.repsDone.isEmpty())
            }
        }
    }

    @Test
    fun `createTraining should create and save training with the correct data`() = runTest {
        viewModel.divisionStatus.getOrAwaitValues {
            viewModel.getActualDivisionToDo("A")
            advanceUntilIdle()
            val divisionForUi = it.last()!!
            println(divisionForUi.exercises.toString())
            for (exercise in divisionForUi.exercises){
                for (serie in 0 until exercise.numOfSeries!!){
                    exercise.repsDone.add(mutableListOf(1,2,3))
                }
            }
            viewModel.createTraining()
            advanceUntilIdle()
            assertEquals(1, fakeRepository.insertWorkoutCalls)
            for(divisionDone in fakeRepository.insertedWorkouts[0].trainingsDone){
                println("wow look at him: ${divisionDone.toString()}")
            }
        }
    }

}