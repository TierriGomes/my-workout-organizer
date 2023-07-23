package com.tierriapps.myworkoutorganizer.feature_main.data.local.datasources

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.test.core.app.ApplicationProvider
import com.tierriapps.myworkoutorganizer.common.values.Constants
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.*

import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@HiltAndroidTest
@ExperimentalCoroutinesApi
class LocalUserPreferencesTest {
    @get:Rule
    val hiltRule = HiltAndroidRule(this)
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    // class under test
    @Inject
    lateinit var localUserPreferences: LocalUserPreferences

    @Before
    fun setUp() {
        hiltRule.inject()
    }

    @After
    fun tearDown(){
        runBlocking { localUserPreferences.clearUserPreferences() }
    }

    @Test
    fun insertAndGetActualWorkoutID_givingAValidID_should_returnTheID()
    = runTest {
        // GIVEN
        val id = 123
        // WHEN
        localUserPreferences.setActualWorkoutId(id)
        advanceUntilIdle()
        val idSaved = localUserPreferences.getActualWorkoutId()
        advanceUntilIdle()
        // THEN
        assertEquals(id, idSaved)
    }

    @Test
    fun getActualWorkoutId_if_thereIsNoSavedID_should_returnNull()
    = runTest {
        // GIVEN
        val expectedId = null
        // WHEN
        val idSaved = localUserPreferences.getActualWorkoutId()
        advanceUntilIdle()
        // THEN
        assertEquals(expectedId, idSaved)
    }
}