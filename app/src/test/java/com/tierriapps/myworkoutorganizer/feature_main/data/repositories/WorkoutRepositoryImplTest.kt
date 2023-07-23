package com.tierriapps.myworkoutorganizer.feature_main.data.repositories

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tierriapps.myworkoutorganizer.feature_main.data.local.datasources.LocalUserPreferences
import com.tierriapps.myworkoutorganizer.feature_main.data.local.datasources.WorkoutLocalDAO
import com.tierriapps.myworkoutorganizer.feature_main.data.remote.datasources.WorkoutRemoteDAO
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
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
    fun `getAllWorkouts when localDataSource is not empty, don't call remote and emit data`()
    = runTest {

    }

    @Test
    fun `getAllWorkouts when localDataSource is empty, call remote and emit data if its not empty`()
    = runTest {

    }

    @Test
    fun `getAllWorkouts when localDataSource is empty, call remote and emit Error if its empty`()
    = runTest {

    }

    
}