package com.tierriapps.myworkoutorganizer.feature_main.presenter.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tierriapps.myworkoutorganizer.common.utils.Resource
import com.tierriapps.myworkoutorganizer.feature_main.domain.usecases.*
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CreateNewWorkoutViewModel @Inject constructor(
    private val createExercise: CreateExercise,
    private val createDivision: CreateDivision,
    private val createWorkout: CreateWorkout,
    private val putExerciseInDivision: PutExerciseInDivision,
    private val saveWorkout: SaveWorkout,
    private val setActualWorkout: SetActualWorkout
): ViewModel() {

    private val _exerciseStatus = MutableLiveData<Resource<String>>()
    val exerciseStatus: LiveData<Resource<String>> = _exerciseStatus

    private val _divisionStatus = MutableLiveData<Resource<String>>()
    val divisionStatus: LiveData<Resource<String>> =_divisionStatus

    private val _workoutStatus = MutableLiveData<Resource<String>>()
    val workoutStatus: LiveData<Resource<String>> = _workoutStatus



}