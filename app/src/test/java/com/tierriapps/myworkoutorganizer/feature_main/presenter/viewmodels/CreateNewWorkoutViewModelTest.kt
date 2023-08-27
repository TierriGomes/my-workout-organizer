package com.tierriapps.myworkoutorganizer.feature_main.presenter.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tierriapps.myworkoutorganizer.common.utils.Resource
import com.tierriapps.myworkoutorganizer.common.utils.UiText
import com.tierriapps.myworkoutorganizer.feature_main.domain.models.Division
import com.tierriapps.myworkoutorganizer.feature_main.domain.models.Exercise
import com.tierriapps.myworkoutorganizer.feature_main.domain.models.Workout
import com.tierriapps.myworkoutorganizer.feature_main.domain.usecases.*
import com.tierriapps.myworkoutorganizer.feature_main.presenter.models.DivisionForUi
import com.tierriapps.myworkoutorganizer.feature_main.presenter.models.ExerciseForUi
import com.tierriapps.myworkoutorganizer.feature_main.utils.DivisionName
import com.tierriapps.myworkoutorganizer.testutils.getOrAwaitValues
import io.mockk.coEvery
import com.tierriapps.myworkoutorganizer.R
import com.tierriapps.myworkoutorganizer.testutils.getOrAwaitValue
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.*
import org.junit.Assert.*

import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.concurrent.TimeoutException

@ExperimentalCoroutinesApi
class CreateNewWorkoutViewModelTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    // class under test
    private lateinit var viewModel: CreateNewWorkoutViewModel
    // mocks
    private val createExercise: CreateExercise = CreateExercise()
    private val createDivision: CreateDivision = CreateDivision()
    private val createWorkout: CreateWorkout = CreateWorkout()
    // mocks
    private val saveWorkout: SaveWorkout = mockk(relaxed = true)
    private val setActualWorkout: SetActualWorkout = mockk(relaxed = true)

    val validExercises = listOf (
        Exercise("exercise1", "desc", 4, 50, weight = 10),
        Exercise("exercise2", "desc", 3, 50, weight = 12)
    )
    val validDivisions = listOf (
        Division(DivisionName.A, "", validExercises.toMutableList())
    )
    val validWorkout = Workout(
        null, "workout", "test", validDivisions
    )

    @Before
    fun setUp() {
        viewModel = CreateNewWorkoutViewModel(
            createExercise, createDivision, createWorkout, saveWorkout, setActualWorkout
        )
    }
    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `addNewDivision will create a newDivision and notify the listOfDivisions`() = runTest {
        // given
        var events: List<MutableList<DivisionForUi>> = listOf()

        // when
        viewModel.listOfDivisions.getOrAwaitValues(
            duringObserve = {
                println(it.toString()) },
            afterObserve = {
                events = it
                viewModel.addNewDivision()
            }
        )
        // then
        assertEquals(2, events.size)
        assertEquals(1, events[0].size)
    }

    @Test
    fun `addNewDivision will create a new division and set it as actualDivision`() = runTest {
        // given
        var events: List<DivisionForUi?> = listOf()

        // when
        viewModel.actualDivision.getOrAwaitValues(
            duringObserve = { println(it.toString()) },
            afterObserve = {
                events = it
                viewModel.addNewDivision()
                viewModel.addNewDivision()
            }
        )
        // then
        assertEquals(2, events.size)
        assertEquals('B', events[1]?.name)
    }
    @Test
    fun `addExerciseInActualDivision will create and add an Exercise and notify it in actualDivision`()
    = runTest {
        // given
        var events: List<DivisionForUi?> = listOf()
        // when
        viewModel.actualDivision.getOrAwaitValues(
            duringObserve = { println("${it?.name}  -  ${it?.exercises?.size}") },
            afterObserve = {
                events = it
                viewModel.addNewDivision()
                assertEquals(0, events[0]!!.exercises.size)
                viewModel.addNewExerciseInActualDivision()
            }
        )
        // then
        assertEquals(2, events.size)
        assertEquals(1, events[1]!!.exercises.size)
    }

    @Test
    fun `addExerciseInActualDivision will create and add an Exercise, change value in listOfDivisions but DON'T notify it`()
    = runTest {
        // given
        var events: List<MutableList<DivisionForUi>> = listOf()
        viewModel.listOfDivisions.getOrAwaitValues(
            duringObserve = { println(it.toString()) },
            afterObserve = {
                events = it
                viewModel.addNewDivision()
                assertEquals(0, events[0][0].exercises.size)
                viewModel.addNewExerciseInActualDivision()
            }
        )
        // then
        assertEquals(2, events.size)
        assertEquals(1, events[1][0].exercises.size)
    }

    @Test
    fun `selectDivision when division is in listOfDivision will change the actualDivision to a division with reference in listOfDivisions`(){
        // given
        var events: List<DivisionForUi?> = listOf()
        var divisionFromList: DivisionForUi? = null
        // when
        viewModel.actualDivision.getOrAwaitValues(
            duringObserve = { println(it?.name) },
            afterObserve = {
                events = it
                viewModel.addNewDivision()
                viewModel.addNewDivision()
                val divisionToSelect = it[0]!!
                divisionToSelect.description = "test"
                viewModel.selectDivision(divisionToSelect)
            }
        )
        viewModel.listOfDivisions.getOrAwaitValues(
            duringObserve = {
                println(it.toString())
                divisionFromList = it[0]
            }
        )
        // then
        assertEquals(3, events.size)
        assertEquals('A', events[0]!!.name)
        assertEquals('B', events[1]!!.name)
        assertEquals('A', events[2]!!.name)
        assertEquals("test", divisionFromList!!.description)
    }
    @Test
    fun `selectDivision when division is NOT in listOfDivision will NOT change the actualDivision`(){
        // given
        var events: List<DivisionForUi?> = listOf()
        // when
        viewModel.actualDivision.getOrAwaitValues(
            duringObserve = { println(it?.name) },
            afterObserve = {
                events = it
                viewModel.addNewDivision()
                viewModel.addNewDivision()
                it[0]?.apply { name = 'A'; description = "" }
                val divisionToSelect = DivisionForUi('A', "")
                viewModel.selectDivision(divisionToSelect)
            }
        )
        // then
        assertEquals(2, events.size)
        assertEquals('A', events[0]!!.name)
        assertEquals('B', events[1]!!.name)
    }

    @Test
    fun `deleteExercise will delete the exercise notify the actualDivision`(){
        // given
        var events: List<DivisionForUi?> = listOf()
        // when
        viewModel.actualDivision.getOrAwaitValues(
            duringObserve = { println(it?.exercises) },
            afterObserve = {
                events = it
                viewModel.addNewDivision()
                viewModel.addNewExerciseInActualDivision()
                viewModel.addNewExerciseInActualDivision()
                val actualExercises = it[2]!!.exercises
                actualExercises[0].name = "first"
                actualExercises[1].name = "second"
                assertEquals(2, actualExercises.size)
                viewModel.removeExercise(actualExercises[0])
            }
        )
        // then
        assertEquals(4, events.size)
        assertEquals(1, events[3]!!.exercises.size)
        assertEquals("second", events[3]!!.exercises[0].name)
    }
    @Test
    fun `deleteExercise will delete the exercise and the empty division and notify actualDivision and listOfDivisions`(){
        // given
        var events: List<DivisionForUi?> = listOf()
        var listEvents: List<MutableList<DivisionForUi>> = listOf()
        // when
        viewModel.listOfDivisions.getOrAwaitValues(
            duringObserve = { println("big list ${it.toString()}") },
            afterObserve =  {
                listEvents = it
                viewModel.actualDivision.getOrAwaitValues(
                    duringObserve = { println(it?.exercises) },
                    afterObserve = {
                        events = it
                        viewModel.addNewDivision()
                        viewModel.addNewExerciseInActualDivision()
                        viewModel.addNewExerciseInActualDivision()
                        val actualExercises = it[2]!!.exercises
                        actualExercises[0].name = "first"
                        actualExercises[1].name = "second"
                        assertEquals(2, actualExercises.size)
                        viewModel.removeExercise(actualExercises[0])
                        assertEquals(1, actualExercises.size)
                        viewModel.removeExercise(actualExercises[0])
                    }
                )
            }
        )
        // then
        assertEquals(5, events.size)
        assertEquals(null, events.last())
        assertEquals(3, listEvents.size)
        assertEquals(0, listEvents[0].size)
    }
    @Test
    fun `deleteExercise don't delete if the exercise is not inside the division`(){
        // given
        var events: List<DivisionForUi?> = listOf()
        // when
        viewModel.actualDivision.getOrAwaitValues(
            duringObserve = { println(it?.exercises) },
            afterObserve = {
                events = it
                viewModel.addNewDivision()
                viewModel.addNewExerciseInActualDivision()
                val actualExercises = it[1]!!.exercises
                actualExercises[0].name = "first"
                val copyExercise = ExerciseForUi("first")
                copyExercise.name = "porra"
                viewModel.removeExercise(copyExercise)
            }
        )
        // then
        assertEquals(2, events.size)
        assertEquals(1, events[1]!!.exercises.size)
        assertEquals("first", events[1]!!.exercises[0].name)
    }

    @Test
    fun `insertDescriptionInDivision insert the description without notifying any livedata`(){
        // given
        var listEvents: List<MutableList<DivisionForUi>> = listOf()
        var events: List<DivisionForUi?> = listOf()
        // when
        viewModel.listOfDivisions.getOrAwaitValues(
            duringObserve = { println(it.toString()) },
            afterObserve = {
                listEvents = it
                viewModel.actualDivision.getOrAwaitValues(
                    duringObserve = { println(it.toString()) },
                    afterObserve = {
                        events = it
                        viewModel.addNewDivision()
                        viewModel.insertDescriptionInDivision("test")
                    }
                )
            }
        )
        assertEquals(2, listEvents.size)
        assertEquals(1, events.size)
        assertEquals("test", events[0]!!.description)
    }

    @Test
    fun `createAndValidate if listOfWorkouts is empty emit an error status`(){
        Dispatchers.setMain(StandardTestDispatcher())
        viewModel.workoutStatus.getOrAwaitValues {
            viewModel.createAndValidateWorkout("asd", "test")
            println(it.toString())
            assertTrue(it[0] is Resource.Error)
        }
    }

    @Test
    fun `createAndValidate workout with valid values will save and set the workout as main`()
    = runTest {
        val dispatcher = StandardTestDispatcher()
        Dispatchers.setMain(dispatcher)
        // given
        coEvery {
            saveWorkout.invoke(any())
        }returns flowOf(Resource.Success(validWorkout, UiText.StringResource(R.string.success)))
        coEvery {
            setActualWorkout.invoke(any())
        }returns flowOf(Resource.Success(1, UiText.StringResource(R.string.success)))
        // when
        viewModel.addNewDivision()
        viewModel.addNewExerciseInActualDivision()
        viewModel.addNewExerciseInActualDivision()
        viewModel.actualDivision.getOrAwaitValues(
            duringObserve = { println(it.toString()) },
            afterObserve = {
                it[0]!!.description = "test"
                val exercises = it[0]!!.exercises
                exercises[0].apply {
                    name = validExercises[0].name
                    description = validExercises[0].description
                    numOfSeries = validExercises[0].numOfSeries
                    timeOfRest = validExercises[0].timeOfRest
                    weight = validExercises[0].weight
                }
                exercises[1].apply {
                    name = validExercises[1].name
                    description = validExercises[1].description
                    numOfSeries = validExercises[1].numOfSeries
                    timeOfRest = validExercises[1].timeOfRest
                    weight = validExercises[1].weight
                }
        })
        // then
        viewModel.workoutStatus.getOrAwaitValues(
            duringObserve = { println(it.toString()) },
            afterObserve = {
                viewModel.createAndValidateWorkout("workout", "test")
                dispatcher.scheduler.advanceUntilIdle()
                println("events on workout status: ${it.size}")
                assertEquals(1, it.size)
                assertTrue(it[0] is Resource.Success)
            }
        )
        val navigate = viewModel.allDoneToNavigate.getOrAwaitValue()
        assertTrue(navigate is Resource.Success)
        assertEquals(1, navigate!!.content)
        coVerify { setActualWorkout.invoke(any()) }
        coVerify { saveWorkout.invoke(any()) }
    }
    @Test
    fun `createAndValidate workout with invalid values will not save and send a error`()
    = runTest {
        val dispatcher = StandardTestDispatcher()
        Dispatchers.setMain(dispatcher)
        // given
        coEvery {
            saveWorkout.invoke(any())
        }returns flowOf(Resource.Success(validWorkout, UiText.StringResource(R.string.success)))
        coEvery {
            setActualWorkout.invoke(any())
        }returns flowOf(Resource.Success(1, UiText.StringResource(R.string.success)))
        // when
        viewModel.addNewDivision()
        viewModel.addNewExerciseInActualDivision()
        viewModel.addNewExerciseInActualDivision()
        viewModel.actualDivision.getOrAwaitValues(
            duringObserve = { println(it.toString()) },
            afterObserve = {
                it[0]!!.description = "test"
                val exercises = it[0]!!.exercises
                exercises[0].apply {
                    name = validExercises[0].name
                    description = validExercises[0].description
                    numOfSeries = validExercises[0].numOfSeries
                    timeOfRest = validExercises[0].timeOfRest
                    weight = validExercises[0].weight
                }
                exercises[1].apply {
                    name = validExercises[1].name
                    description = validExercises[1].description
                    numOfSeries = validExercises[1].numOfSeries
                    timeOfRest = 999999 // invalid value
                    weight = validExercises[1].weight
                }
            })
        // then
        viewModel.workoutStatus.getOrAwaitValues(
            duringObserve = { println(it.toString()) },
            afterObserve = {
                viewModel.createAndValidateWorkout("workout", "test")
                dispatcher.scheduler.advanceUntilIdle()
                println("events on workout status: ${it.size}")
                assertEquals(1, it.size)
                assertTrue(it[0] is Resource.Error)
            }
        )
        assertThrows(TimeoutException::class.java){ viewModel.allDoneToNavigate.getOrAwaitValue() }
        coVerify(inverse = true) { setActualWorkout.invoke(any()) }
        coVerify(inverse = true) { saveWorkout.invoke(any()) }
    }
}