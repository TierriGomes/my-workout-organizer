package com.tierriapps.myworkoutorganizer.feature_main.presenter.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import org.junit.Assert.*

import org.junit.After
import org.junit.Before
import org.junit.Rule

@ExperimentalCoroutinesApi
class DoTrainingSessionViewModelTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()
    private val dispatcher = StandardTestDispatcher()
    // class under test
    private lateinit var viewModel: DoTrainingSessionViewModel
    
    @Before
    fun setUp() {
    }

    @After
    fun tearDown() {
    }
}