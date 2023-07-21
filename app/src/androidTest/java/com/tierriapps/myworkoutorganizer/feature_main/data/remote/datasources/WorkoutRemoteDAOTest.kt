package com.tierriapps.myworkoutorganizer.feature_main.data.remote.datasources

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.firebase.FirebaseException
import com.tierriapps.myworkoutorganizer.feature_main.data.remote.dto.WorkoutRemoteEntity
import dagger.hilt.android.testing.HiltAndroidRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*

import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

/**
 *  testing the real api here
 *  the main objective is verify our api rules set in FireBase service
 */
@ExperimentalCoroutinesApi
class WorkoutRemoteDAOTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()
    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    // class under test
    @Inject
    private lateinit var workoutRemoteDAO: WorkoutRemoteDAO

    private val fakeOwnerId = "user123"

    @Before
    fun setUp() {
        hiltRule.inject()
        // clean data before each test
        runBlocking {
            val savedWorkouts = workoutRemoteDAO.getAllWorkoutEntities(fakeOwnerId)?: listOf()
            for (workout in savedWorkouts){
                workoutRemoteDAO.deleteWorkoutEntity(workout)
            }
        }
    }
    @Test
    fun getWorkoutRemoteEntityById_givingExistentParams_should_returnTheWorkout()
    = runBlocking {
        // GIVEN
        val workoutToAdd = WorkoutRemoteEntity(fakeOwnerId, 1, "test")
        workoutRemoteDAO.insertWorkoutEntity(workoutToAdd)
        // WHEN
        val workoutFromApi = workoutRemoteDAO.getWorkoutEntityByID(1, fakeOwnerId)
        // THEN
        assertEquals(workoutToAdd.name, workoutFromApi?.name)
    }

    @Test
    fun getWorkoutRemoteEntityById_givingNonexistentParams_should_throwFireBaseException()
    = runBlocking {
        // GIVEN
        val workoutToAdd = WorkoutRemoteEntity(fakeOwnerId, 1, "test")
        workoutRemoteDAO.insertWorkoutEntity(workoutToAdd)
        // WHEN
        var workoutFromApi: WorkoutRemoteEntity? = null
        // THEN
        assertThrows(FirebaseException::class.java){
            runBlocking {
                workoutFromApi = workoutRemoteDAO.getWorkoutEntityByID(0, fakeOwnerId)
            }
        }
        assertEquals(null, workoutFromApi)
    }

    @Test
    fun getAllRemoteWorkouts_givingExistentOwnerId_should_returnAListWithAllSavedWorkouts()
    = runBlocking{
        // GIVEN
        val workoutToAdd1 = WorkoutRemoteEntity(fakeOwnerId, 1, "test1")
        val workoutToAdd2 = WorkoutRemoteEntity(fakeOwnerId, 2, "test2")
        workoutRemoteDAO.insertWorkoutEntity(workoutToAdd1)
        workoutRemoteDAO.insertWorkoutEntity(workoutToAdd2)
        // WHEN
        val workoutsFromApi = workoutRemoteDAO.getAllWorkoutEntities(fakeOwnerId)!!
        val workoutFromApi1 = workoutsFromApi[0]
        val workoutFromApi2 = workoutsFromApi[1]
        // THEN
        assertEquals(workoutToAdd1.name, workoutFromApi1.name)
        assertEquals(workoutToAdd2.name, workoutFromApi2.name)
    }

    @Test
    fun getAllRemoteWorkouts_givingNonexistentOwnerId_should_throwFireBaseException()
     = runBlocking {
        // GIVEN
        val workoutToAdd1 = WorkoutRemoteEntity(fakeOwnerId, 1, "test1")
        val workoutToAdd2 = WorkoutRemoteEntity(fakeOwnerId, 2, "test2")
        workoutRemoteDAO.insertWorkoutEntity(workoutToAdd1)
        workoutRemoteDAO.insertWorkoutEntity(workoutToAdd2)
        // WHEN
        var workoutFromApi1: WorkoutRemoteEntity? = null
        var workoutFromApi2: WorkoutRemoteEntity? = null
        assertThrows(FirebaseException::class.java){
            runBlocking {
                val workoutsFromApi = workoutRemoteDAO.getAllWorkoutEntities("noneExistent")!!
                workoutFromApi1 = workoutsFromApi[0]
                workoutFromApi2 = workoutsFromApi[1]
            }
        }
        assertEquals(null, workoutFromApi1)
        assertEquals(null, workoutFromApi2)
    }

    @Test
    fun insertWorkoutRemoteEntity_giving_aNotSavedYetValidWorkout_should_addIt()
    = runBlocking {
        // GIVEN
        val workoutsCountBefore = try {
            workoutRemoteDAO.getAllWorkoutEntities(fakeOwnerId)!!.size
        }catch (e: java.lang.Exception){
            0
        }
        val workoutToAdd = WorkoutRemoteEntity(fakeOwnerId, 1, "test1")
        // WHEN
        workoutRemoteDAO.insertWorkoutEntity(workoutToAdd)
        val workoutsCountAfter = try {
            workoutRemoteDAO.getAllWorkoutEntities(fakeOwnerId)!!.size
        }catch (e: java.lang.Exception){
            0
        }
        // THEN
        assertEquals(workoutsCountBefore, workoutsCountAfter-1)

    }

    @Test
    fun insertWorkoutRemoteEntity_giving_aAlreadySavedValidWorkout_should_setIt(){

    }

    @Test
    fun insertWorkoutRemoteEntity_giving_aInvalidWorkout_should_throwFirebaseException(){

    }

    @Test
    fun deleteWorkoutRemoteEntity_giving_aAlreadySavedValidWorkout_should_deleteIt(){

    }

    @Test
    fun deleteWorkoutRemoteEntity_giving_aNotSavedValidWorkout_should_throwFireBaseException(){

    }

}