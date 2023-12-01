package com.tierriapps.myworkoutorganizer.feature_main.data.repositories

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.firebase.FirebaseException
import com.tierriapps.myworkoutorganizer.common.utils.Resource
import com.tierriapps.myworkoutorganizer.common.values.Constants
import com.tierriapps.myworkoutorganizer.feature_main.data.local.datasources.LocalUserPreferences
import com.tierriapps.myworkoutorganizer.feature_main.data.local.datasources.WorkoutLocalDAO
import com.tierriapps.myworkoutorganizer.feature_main.data.local.dto.WorkoutRoomEntity
import com.tierriapps.myworkoutorganizer.feature_main.data.local.dto.toRoomEntity
import com.tierriapps.myworkoutorganizer.feature_main.data.remote.datasources.WorkoutRemoteDAO
import com.tierriapps.myworkoutorganizer.feature_main.data.remote.dto.WorkoutRemoteEntity
import com.tierriapps.myworkoutorganizer.feature_main.data.remote.dto.toRemoteEntity
import com.tierriapps.myworkoutorganizer.feature_main.domain.models.Workout
import com.tierriapps.myworkoutorganizer.testutils.getOrAwaitValues
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class WorkoutRepositoryImplTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()
    // mocks
    private val remoteWorkoutDAO: WorkoutRemoteDAO = mockk(relaxed = true)
    private val localWorkoutDAO: WorkoutLocalDAO = mockk(relaxed = true)
    private val localPreferences: LocalUserPreferences = mockk(relaxed = true)
    // class under test
    private lateinit var workoutRepositoryImpl: WorkoutRepositoryImpl

    @Before
    fun setUp(){
        workoutRepositoryImpl = WorkoutRepositoryImpl(
            remoteWorkoutDAO,
            localWorkoutDAO,
            localPreferences
        )
    }

    @Test
    fun `getAllWorkouts, when localDataSource is not empty, don't call remote and emit Success`()
    = runBlocking {
        // GIVEN
        coEvery {
            localWorkoutDAO.getAllWorkoutEntities()
        } returns listOf(WorkoutRoomEntity(1, "test"))
        // WHEN
        val result = workoutRepositoryImpl.getAllWorkouts().toList()
        val loading = result[0]
        val success = result[1]
        val dataName = result[1]!!.content!![0].name
        // THEN
        assertEquals(2, result.size)
        assertTrue(loading is Resource.Loading)
        assertTrue(success is Resource.Success)
        assertEquals("test", dataName)
        coVerify(inverse = true) { remoteWorkoutDAO.getAllWorkoutEntities(any()) }
    }

    @Test
    fun `getAllWorkouts, when local is empty, fill out Local calling remote and emit Success`()
    = runBlocking {
        // GIVEN
        coEvery {
            localWorkoutDAO.getAllWorkoutEntities()
        } returns listOf()
        coEvery {
            remoteWorkoutDAO.getAllWorkoutEntities(any())
        } returns listOf(
            WorkoutRemoteEntity("ownerId", 1, "test1"),
            WorkoutRemoteEntity("ownerId", 2, "test2")
        )
        // WHEN
        val result = workoutRepositoryImpl.getAllWorkouts().toList()
        val loading = result[0]
        val success = result[1]
        val dataName = result[1]!!.content!![1].name
        // THEN
        coVerify { remoteWorkoutDAO.getAllWorkoutEntities(any()) }
        coVerify(atLeast = 2, atMost = 2) { localWorkoutDAO.insertWorkoutEntity(any()) }
        assertEquals(2, result.size)
        assertTrue(loading is Resource.Loading)
        assertTrue(success is Resource.Success)
        assertEquals("test2", dataName)
    }

    @Test
    fun `getAllWorkouts, when local is empty, call remote and emit Error if its empty`()
    = runBlocking {
        coEvery {
            localWorkoutDAO.getAllWorkoutEntities()
        } returns listOf()
        coEvery {
            remoteWorkoutDAO.getAllWorkoutEntities(any())
        } returns listOf()
        // WHEN
        val result = workoutRepositoryImpl.getAllWorkouts().toList()
        val loading = result[0]
        val error = result[1]
        // THEN
        coVerify { remoteWorkoutDAO.getAllWorkoutEntities(any()) }
        assertEquals(2, result.size)
        assertTrue(loading is Resource.Loading)
        assertTrue(error is Resource.Error)
    }

    @Test
    fun `getWorkoutById, when local is not empty, don't call remote and emit success`()
    = runBlocking {
        // GIVEN
        coEvery {
            localWorkoutDAO.getWorkoutEntityByID(any())
        } returns WorkoutRoomEntity(1, "test")
        // WHEN
        val result = workoutRepositoryImpl.getWorkoutById(1).toList()
        val first = result[0]
        val last = result[1]
        // THEN
        coVerify(inverse = true) { remoteWorkoutDAO.getWorkoutEntityByID(any(), any()) }
        assertEquals(2, result.size)
        assertTrue(first is Resource.Loading)
        assertTrue(last is Resource.Success)
        assertEquals("test", last.content?.name)
    }

    @Test
    fun `getWorkoutById, when local is empty, don't call remote and emit Error`()
    = runBlocking {
        // GIVEN
        coEvery {
            localWorkoutDAO.getWorkoutEntityByID(any())
        } returns null
        coEvery {
            remoteWorkoutDAO.getWorkoutEntityByID(any(), any())
        } returns WorkoutRemoteEntity("ownerId", 1, )

        // WHEN
        val result = workoutRepositoryImpl.getWorkoutById(1).toList()
        val first = result[0]
        val last = result[1]
        // THEN
        coVerify(inverse = true) { remoteWorkoutDAO.getWorkoutEntityByID(any(), any()) }
        assertEquals(2, result.size)
        assertTrue(first is Resource.Loading)
        assertTrue(last is Resource.Error)
    }

    @Test
    fun `insertWorkout, when workout is valid, call remote and local and emit Success`()
    = runBlocking {
        // GIVEN
        Constants.USER_ID = "testId"
        val validWorkout = Workout(1, "test")
        // WHEN
        val result = workoutRepositoryImpl.insertWorkout(validWorkout).toList()[1]
        // THEN
        coVerify { localWorkoutDAO.insertWorkoutEntity(validWorkout.toRoomEntity()) }
        coVerify {
            remoteWorkoutDAO.insertWorkoutEntity(validWorkout.toRemoteEntity("testId"))
        }
        assertTrue(result is Resource.Success)
    }

    @Test
    fun `insertWorkout, when cannot save in local, don't call remote and emit Error`()
    = runBlocking {
        // GIVEN
        coEvery { localWorkoutDAO.insertWorkoutEntity(any()) } throws Exception("error")
        Constants.USER_ID = "testId"
        val validWorkout = Workout(1, "test")
        // WHEN
        val result = workoutRepositoryImpl.insertWorkout(validWorkout).toList()[1]
        // THEN
        coVerify { localWorkoutDAO.insertWorkoutEntity(validWorkout.toRoomEntity()) }
        coVerify (inverse = true) {
            remoteWorkoutDAO.insertWorkoutEntity(validWorkout.toRemoteEntity("testId"))
        }
        assertTrue(result is Resource.Error)
    }

    @Test
    fun `insertWorkout, when cannot save in remote, will emit a Error with data and call Preferences`()
    = runBlocking {
        // GIVEN
        coEvery { localWorkoutDAO.insertWorkoutEntity(any()) } returns Unit
        coEvery { remoteWorkoutDAO.insertWorkoutEntity(any()) } throws FirebaseException("error")
        Constants.USER_ID = "testId"
        val validWorkout = Workout(1, "test")
        // WHEN
        val result = workoutRepositoryImpl.insertWorkout(validWorkout).toList()[1]
        // THEN
        coVerify { localWorkoutDAO.insertWorkoutEntity(validWorkout.toRoomEntity()) }
        coVerify {
            remoteWorkoutDAO.insertWorkoutEntity(validWorkout.toRemoteEntity("testId"))
        }
        coVerify { localPreferences.setPendingTasksInRemote(true) }
        assertTrue(result is Resource.Error)
        assertEquals("test", result.content?.name)
    }

    @Test
    fun `deleteWorkout, when there is a workout to delete, will emit Success and call remote and local`()
    = runBlocking {
        // GIVEN
        coEvery { localWorkoutDAO.deleteWorkoutEntity(any()) } returns Unit
        coEvery { remoteWorkoutDAO.deleteWorkoutEntity(any()) } returns Unit
        val workoutToDelete = Workout()
        // WHEN
        val result = workoutRepositoryImpl.deleteWorkout(workoutToDelete).toList()[1]
        // THEN
        coVerify { localWorkoutDAO.deleteWorkoutEntity(any()) }
        coVerify { remoteWorkoutDAO.deleteWorkoutEntity(any()) }
        assertTrue(result is Resource.Success)
    }

    @Test
    fun `deleteWorkout, cannot delete in local, try to delete in remote`()
    = runBlocking {
        // GIVEN
        coEvery { localWorkoutDAO.deleteWorkoutEntity(any()) } throws Exception("")
        coEvery { remoteWorkoutDAO.deleteWorkoutEntity(any()) } returns Unit
        val workoutToDelete = Workout()
        // WHEN
        val result = workoutRepositoryImpl.deleteWorkout(workoutToDelete).toList()[1]
        // THEN
        coVerify { localWorkoutDAO.deleteWorkoutEntity(any()) }
        coVerify { remoteWorkoutDAO.deleteWorkoutEntity(any()) }
        assertTrue(result is Resource.Success)
    }

    @Test
    fun `deleteWorkout, cannot delete in remote, try to delete in local and call Preferences`()
    = runBlocking {
        // GIVEN
        coEvery { localWorkoutDAO.deleteWorkoutEntity(any()) } returns Unit
        coEvery { remoteWorkoutDAO.deleteWorkoutEntity(any()) } throws Exception("")
        val workoutToDelete = Workout()
        // WHEN
        val result = workoutRepositoryImpl.deleteWorkout(workoutToDelete).toList()[1]
        // THEN
        coVerify { localWorkoutDAO.deleteWorkoutEntity(any()) }
        coVerify { remoteWorkoutDAO.deleteWorkoutEntity(any()) }
        coVerify { localPreferences.setPendingTasksInRemote(true) }
        assertTrue(result is Resource.Error)
    }
    @Test
    fun `deleteWorkout, cannot delete in remote and local, emit Error`()
            = runBlocking {
        // GIVEN
        coEvery { localWorkoutDAO.deleteWorkoutEntity(any()) } throws Exception("")
        coEvery { remoteWorkoutDAO.deleteWorkoutEntity(any()) } throws Exception("")
        val workoutToDelete = Workout()
        // WHEN
        val result = workoutRepositoryImpl.deleteWorkout(workoutToDelete).toList()[1]
        // THEN
        coVerify { localWorkoutDAO.deleteWorkoutEntity(any()) }
        coVerify { remoteWorkoutDAO.deleteWorkoutEntity(any()) }
        assertTrue(result is Resource.Error)
    }
    @Test
    fun `getActualWorkout, when there is an ActualWorkout, will call preferences and return Success`()
    = runBlocking {
        // GIVEN
        coEvery { localPreferences.getActualWorkoutId() } returns 1
        // WHEN
        val result = workoutRepositoryImpl.getActualWorkoutId()
        // THEN
        assertEquals(1, result.content)
    }

    @Test
    fun `getActualWorkout, when there is no ActualWorkout, will call preferences and return Error`()
    = runBlocking {
        // GIVEN
        coEvery { localPreferences.getActualWorkoutId() } returns null
        // WHEN
        val result = workoutRepositoryImpl.getActualWorkoutId()
        // THEN
        assertTrue(result is Resource.Error)
    }

    @Test
    fun `setActualWorkout, will call preferences and return Success if it can save`()
    = runBlocking {
        // GIVEN
        coEvery { localPreferences.setActualWorkoutId(1) } returns Unit
        // WHEN
        val result = workoutRepositoryImpl.setActualWorkoutId(1)
        // THEN
        assertTrue(result is Resource.Success)
    }

    @Test
    fun `setActualWorkout, will call preferences and return Error if it cannot save`()
    = runBlocking {
        // GIVEN
        coEvery { localPreferences.setActualWorkoutId(1) } throws Exception("")
        // WHEN
        val result = workoutRepositoryImpl.setActualWorkoutId(1)
        // THEN
        assertTrue(result is Resource.Error)
    }

    @Test
    fun `getUserId should return the updated value of userIdConstant`()
    = runTest {
        // GIVEN
        Constants.USER_ID = "test1"
        val list = mutableListOf<String>()
        // WHEN
        workoutRepositoryImpl.getUserId().getOrAwaitValues(
            duringObserve = {
                list.add(it)
            },
            afterObserve = {
                advanceUntilIdle()
            }
        )
        // THEN
        assertEquals("test1", list[0])
    }

    @Test
    fun `getUserName should return the updated value of userNameConstant`()
    = runTest {
        // GIVEN
        Constants.USER_NAME = "test1"
        val list = mutableListOf<String>()
        // WHEN
        workoutRepositoryImpl.getUserName().getOrAwaitValues(
            duringObserve = {
                list.add(it)
            },
            afterObserve = {
                advanceUntilIdle()
            }
        )
        // THEN
        assertEquals("test1", list[0])
    }
}