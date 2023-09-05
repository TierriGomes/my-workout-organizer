package com.tierriapps.myworkoutorganizer.feature_main.presenter.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tierriapps.myworkoutorganizer.common.utils.Resource
import com.tierriapps.myworkoutorganizer.feature_main.domain.models.Workout
import com.tierriapps.myworkoutorganizer.feature_main.domain.usecases.GetActualWorkout
import com.tierriapps.myworkoutorganizer.feature_main.presenter.models.DivisionForUi
import com.tierriapps.myworkoutorganizer.feature_main.presenter.models.toDivisionForUi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getActualWorkout: GetActualWorkout
): ViewModel() {

    private val _actualWorkout = MutableLiveData<Resource<Workout?>>()
    val actualWorkout: LiveData<Resource<Workout?>> = _actualWorkout

    private val _divisionsForm = MutableLiveData<List<DivisionForUi>>()
    val divisionsForm: LiveData<List<DivisionForUi>> = _divisionsForm

    private val _actualTrainings = MutableLiveData<List<DivisionForUi>>()
    val actualTrainings: LiveData<List<DivisionForUi>> = _actualTrainings

    private var divisionsDone: List<DivisionForUi> = listOf()

    fun getActualWorkoutAndSetValues(){
        viewModelScope.launch {
            val actual = getActualWorkout.invoke().onEach {
                _actualWorkout.value = it
                if (it is Resource.Success && it.content != null){
                    _divisionsForm.value = it.content.divisions.map { it.toDivisionForUi() }
                    divisionsDone = it.content.trainingsDone.map {division ->
                        val position = it.content.trainingsDone.indexOf(division)
                        division.toDivisionForUi().apply { day = position } }
                    if (divisionsDone.isEmpty()){
                        divisionsDone = it.content!!.divisions!!.map { it.toDivisionForUi() }
                    }
                    selectDivision(divisionsDone.last())
                }
            }.collect()
        }
    }
    fun selectDivision(divisionForUi: DivisionForUi) {
        val trainingsDoneOfTheDivision = divisionsDone.filter { it.name == divisionForUi.name }
        if (trainingsDoneOfTheDivision.isNotEmpty()){
            _actualTrainings.value = trainingsDoneOfTheDivision
        }
    }

    fun getNextTrainingToDo(): DivisionForUi?{
        val name = divisionsDone.last().name
        val divisions = _divisionsForm.value?:return null
        var index = divisions.indexOf(
            divisions.find { it.name == name }
        )
        index = if (index == divisions.lastIndex) 0 else index
        return divisions[index]
    }

    fun getActualDivisionName(): String{
        return _actualTrainings.value?.get(0)?.name.toString()
    }
}