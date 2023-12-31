package com.tierriapps.myworkoutorganizer.feature_main.presenter.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tierriapps.myworkoutorganizer.common.utils.Resource
import com.tierriapps.myworkoutorganizer.feature_main.domain.models.Division
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

    private var trainingsDone: List<Division> = listOf()

    fun getActualWorkoutAndSetValues(){
        viewModelScope.launch {
            val actual = getActualWorkout.invoke().onEach {
                _actualWorkout.value = it
                if (it is Resource.Success && it.content != null){
                    _divisionsForm.value = it.content.divisions.map { it.toDivisionForUi() }
                    val div = it.content.divisions.map { it.toDivisionForUi() }.toMutableList()
                    for (d in it.content.trainingsDone){
                        div.add(d.toDivisionForUi())
                    }
                    divisionsDone = div
                    trainingsDone = it.content.trainingsDone
                    it.content.trainingsDone.map {division ->
                        val position = it.content.trainingsDone.indexOf(division)
                        division.toDivisionForUi().apply { day = position } }
                    if (divisionsDone.isNotEmpty()){
                        selectDivision(divisionsDone.last())
                    }
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
        if (divisionsDone.isEmpty()){return null}
        val name = divisionsDone.last().name
        val divisions = _divisionsForm.value?:return null
        var index = divisions.indexOf(
            divisions.find { it.name == name }
        )
        if (divisions.lastIndex == index){
            index = 0
        }else {
            index += 1
        }
        return divisions[index]
    }

    fun getActualDivisionName(): String?{
        val name = _actualTrainings.value?.get(0)?.name
        return if (name == null) null else name.toString()
    }

    fun getPositionByAllDivisions(filteredPosition: Int, char: Char):Int? {
        var count = 0
        for (iten in trainingsDone.withIndex()){
            if (iten.value.name.char == char){
                if (count == filteredPosition-1){
                    return iten.index
                }else {
                    count ++
                }
            }
        }
        return null
    }
}