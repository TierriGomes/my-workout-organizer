package com.tierriapps.myworkoutorganizer.feature_main.presenter.viewmodels

import android.util.Log
import androidx.lifecycle.*
import com.tierriapps.myworkoutorganizer.R
import com.tierriapps.myworkoutorganizer.common.utils.Resource
import com.tierriapps.myworkoutorganizer.common.utils.UiText
import com.tierriapps.myworkoutorganizer.common.utils.containsSameReference
import com.tierriapps.myworkoutorganizer.feature_main.domain.models.Division
import com.tierriapps.myworkoutorganizer.feature_main.domain.models.Exercise
import com.tierriapps.myworkoutorganizer.feature_main.domain.models.Workout
import com.tierriapps.myworkoutorganizer.feature_main.domain.usecases.*
import com.tierriapps.myworkoutorganizer.feature_main.presenter.models.DivisionForCreateWorkout
import com.tierriapps.myworkoutorganizer.feature_main.presenter.models.ExerciseForCreateWorkout
import com.tierriapps.myworkoutorganizer.feature_main.utils.DivisionName
import com.tierriapps.myworkoutorganizer.feature_main.utils.getDivisionByChar
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateNewWorkoutViewModel @Inject constructor(
    private val createExercise: CreateExercise,
    private val createDivision: CreateDivision,
    private val createWorkout: CreateWorkout,
    private val saveWorkout: SaveWorkout,
    private val setActualWorkout: SetActualWorkout
): ViewModel() {

    private val _listOfDivisions = MutableLiveData(mutableListOf<DivisionForCreateWorkout>())
    val listOfDivisions: LiveData<MutableList<DivisionForCreateWorkout>> = _listOfDivisions

    private val _actualDivision = MutableLiveData<DivisionForCreateWorkout?>()
    val actualDivision: LiveData<DivisionForCreateWorkout?> = _actualDivision

    private val _workoutStatus = MutableLiveData<Resource<Workout?>>()
    val workoutStatus: LiveData<Resource<Workout?>> = _workoutStatus

    private val _allDoneToNavigate = MutableLiveData<Resource<Int?>>()
    val allDoneToNavigate: LiveData<Resource<Int?>> = _allDoneToNavigate

    var autSavingWorkout = false

    fun addNewDivision(){
        val values = _listOfDivisions.value?: mutableListOf()
        val name = DivisionName.values()[_listOfDivisions.value!!.size]
        values.add(DivisionForCreateWorkout(name.char, ""))
        _listOfDivisions.value = values
        _actualDivision.value = _listOfDivisions.value?.last()
    }

    fun addNewExerciseInActualDivision(){
        val value = _actualDivision.value?:return
        val index = _listOfDivisions.value?.size?:0
        value.exercises.add(ExerciseForCreateWorkout())
        _actualDivision.value = value
    }

    fun selectDivision(division: DivisionForCreateWorkout){
        val value = _listOfDivisions.value?: return
        _actualDivision.value = if (value.containsSameReference(division)) division else return
    }

    fun removeExercise(exercise: ExerciseForCreateWorkout){
        val value = _actualDivision.value?:return
        val allValues = _listOfDivisions.value?: return
        val position = allValues.indexOf(value)
        try {
            if (!(value.exercises.containsSameReference(exercise))){
                return
            }
            value.exercises.remove(exercise)
            if (value.exercises.isEmpty()){
                allValues.remove(value)
                for(i in allValues.withIndex()){
                    i.value.name = DivisionName.values()[i.index].char
                }
                _actualDivision.value = when {
                    position < allValues.size -> allValues[position]
                    position == allValues.size  && allValues.isNotEmpty() -> allValues[position-1]
                    else -> null
                }
                _listOfDivisions.value = allValues
            } else {
                _actualDivision.value = value
            }
        }catch (e: Exception){
            println(e)
        }
    }
    fun insertDescriptionInDivision(description: String){
        actualDivision.value?.description = description
    }

    fun createAndValidateWorkout(workoutName: String, workoutDescription: String){
        val divisionsFromUI = _listOfDivisions.value
        if (divisionsFromUI == null || divisionsFromUI.isEmpty()){
            _workoutStatus.value = Resource.Error(null, UiText.StringResource(R.string.empty_fields))
            return
        }
        var actualValuePosition = divisionsFromUI.indexOf(_actualDivision.value)
        val listOfDivisions = mutableListOf<Division?>()
        for(d in divisionsFromUI){
            val listOfExercises = mutableListOf<Exercise?>()
            for (exercise in d.exercises){
                val exerciseResult = createExercise.invoke(
                    exercise.name?:"", exercise.description?:"",
                    exercise.numOfSeries?:0, exercise.timeOfRest?:0,
                    exercise.weight?:0, exercise.type)
                exercise.status = exerciseResult is Resource.Success
                exercise.message = exerciseResult.message
                listOfExercises.add(exerciseResult.content)
            }
            val divisionResult = createDivision.invoke(getDivisionByChar(d.name), d.description, listOfExercises)
            d.status = divisionResult is Resource.Success
            d.message = divisionResult.message
            if (divisionResult is Resource.Error){
                actualValuePosition = divisionsFromUI.indexOf(d)
            }
            listOfDivisions.add(divisionResult.content)
        }
        val workoutResult = createWorkout.invoke(workoutName, workoutDescription, listOfDivisions)
        viewModelScope.launch {
            if (workoutResult.content != null && workoutResult is Resource.Success){
                println("workout created by viewModel, trying to save..")
                saveWorkout.invoke(workoutResult.content).onEach {
                    _workoutStatus.value = it
                }.collect()
                setActualWorkout.invoke(workoutResult.content).onEach {
                    _allDoneToNavigate.value = it
                }.collect()
            }else {
                println("cannot create workout")
                _workoutStatus.value = workoutResult
                _listOfDivisions.value = divisionsFromUI
                _actualDivision.value = divisionsFromUI[actualValuePosition]
            }
        }
    }




}