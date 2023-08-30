package com.tierriapps.myworkoutorganizer.feature_main.presenter.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tierriapps.myworkoutorganizer.feature_main.domain.usecases.GetActualWorkout
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.*

import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class MainViewModelTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    // class under test
    private lateinit var viewModel: MainViewModel

    private val getActualWorkout: GetActualWorkout = mockk(relaxed = true)

}