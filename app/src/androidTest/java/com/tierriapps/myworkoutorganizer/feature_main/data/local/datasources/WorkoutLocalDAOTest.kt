package com.tierriapps.myworkoutorganizer.feature_main.data.local.datasources

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tierriapps.myworkoutorganizer.feature_main.data.local.dto.WorkoutRoomEntity
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@HiltAndroidTest
@ExperimentalCoroutinesApi
class WorkoutLocalDAOTest {
    @get:Rule
    val hiltRule = HiltAndroidRule(this)
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    // class under test
    @Inject
    lateinit var workoutLocalDAO: WorkoutLocalDAO

    @Before
    fun setUp(){
        hiltRule.inject()
    }

    @Test
    fun insertWorkoutEntity_giving_aNoneIdWorkout_should_addItGivingTheLastID()
    = runBlocking {
        // GIVEN
        val workout1 = WorkoutRoomEntity(5, "name1")
        val workout2 = WorkoutRoomEntity(null, "name2")
        workoutLocalDAO.insertWorkoutEntity(workout1)
        // WHEN
        workoutLocalDAO.insertWorkoutEntity(workout2)
        val workouts = workoutLocalDAO.getAllWorkoutEntities()
        // THEN
        assertEquals(5, workouts[0].id)
        assertEquals(6, workouts[1].id)
        assertEquals("name2", workouts[1].name)
    }

    @Test
    fun insertWorkoutEntity_giving_anAlreadyInsertedWorkout_should_replaceIt()
    = runBlocking {
        // GIVEN
        val workout1 = WorkoutRoomEntity(5, "name1")
        val workout2 = WorkoutRoomEntity(5, "name2")
        workoutLocalDAO.insertWorkoutEntity(workout1)
        // WHEN
        workoutLocalDAO.insertWorkoutEntity(workout2)
        val workouts = workoutLocalDAO.getAllWorkoutEntities()
        // THEN
        assertEquals(1, workouts.size)
    }
    @Test
    fun getAllWorkouts_inAnEmptyDataBase_should_returnAEmptyList()
    = runBlocking {
        // GIVEN an empty data base
        // WHEN
        val workouts = workoutLocalDAO.getAllWorkoutEntities()
        // THEN
        assertTrue(workouts.isEmpty())
    }

    @Test
    fun getAllWorkouts_inAFilledDataBase_should_returnAllWorkouts()
    = runBlocking {
        // GIVEN
        val workout1 = WorkoutRoomEntity(2, "name1")
        val workout2 = WorkoutRoomEntity(null, "name2")
        val workout3 = WorkoutRoomEntity(8, "name1")
        val workout4 = WorkoutRoomEntity(null)
        workoutLocalDAO.insertWorkoutEntity(workout1)
        workoutLocalDAO.insertWorkoutEntity(workout2)
        workoutLocalDAO.insertWorkoutEntity(workout3)
        workoutLocalDAO.insertWorkoutEntity(workout4)
        // WHEN
        val workouts = workoutLocalDAO.getAllWorkoutEntities()
        // THEN
        assertEquals(4, workouts.size)
    }

    @Test
    fun getWorkoutById_giving_aSavedWorkoutId_should_returnTheWorkout()
    = runBlocking {
        // GIVEN
        val workout = WorkoutRoomEntity(null, "test123")
        workoutLocalDAO.insertWorkoutEntity(workout)
        // WHEN
        val savedWorkout = workoutLocalDAO.getWorkoutEntityByID(1)
        // THEN
        assertEquals(workout.name, savedWorkout?.name)
    }

    @Test
    fun getWorkoutById_giving_aNotSavedWorkoutId_should_returnTheWorkout()
    = runBlocking {
        // GIVEN
        val workout = WorkoutRoomEntity(null)
        workoutLocalDAO.insertWorkoutEntity(workout)
        // WHEN
        val savedWorkout = workoutLocalDAO.getWorkoutEntityByID(2)
        // THEN
        assertEquals(null, savedWorkout)
    }

    @Test
    fun deleteWorkout_giving_aInsertedWorkout_should_deleteIt()
    = runBlocking {
        // GIVEN
        val workout = WorkoutRoomEntity(1)
        workoutLocalDAO.insertWorkoutEntity(workout)
        val beforeDeleteCount = workoutLocalDAO.getAllWorkoutEntities().size
        // WHEN
        workoutLocalDAO.deleteWorkoutEntity(workout)
        val afterDeleteCount = workoutLocalDAO.getAllWorkoutEntities().size
        // THEN
        assertEquals(beforeDeleteCount, afterDeleteCount+1)
    }

    @Test
    fun deleteWorkout_giving_aNotInsertedWorkout_should_throwException()
            = runBlocking {
        // GIVEN
        val workout = WorkoutRoomEntity(null)
        workoutLocalDAO.insertWorkoutEntity(workout)
        val beforeDeleteCount = workoutLocalDAO.getAllWorkoutEntities().size
        // WHEN
        workoutLocalDAO.deleteWorkoutEntity(workout)
        val afterDeleteCount = workoutLocalDAO.getAllWorkoutEntities().size
        // THEN
        assertEquals(beforeDeleteCount, afterDeleteCount)
    }
}