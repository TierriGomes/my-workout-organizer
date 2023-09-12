package com.tierriapps.myworkoutorganizer.feature_main.presenter.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tierriapps.myworkoutorganizer.common.utils.Resource
import com.tierriapps.myworkoutorganizer.common.utils.UiText
import com.tierriapps.myworkoutorganizer.feature_main.domain.models.Division
import com.tierriapps.myworkoutorganizer.feature_main.domain.models.Exercise
import com.tierriapps.myworkoutorganizer.feature_main.domain.models.Reps
import com.tierriapps.myworkoutorganizer.feature_main.domain.models.Workout
import com.tierriapps.myworkoutorganizer.feature_main.domain.usecases.GetActualWorkout
import com.tierriapps.myworkoutorganizer.feature_main.domain.usecases.PutDivisionTrainedInWorkout
import com.tierriapps.myworkoutorganizer.feature_main.domain.usecases.SaveWorkout
import com.tierriapps.myworkoutorganizer.feature_main.presenter.models.DivisionForUi
import com.tierriapps.myworkoutorganizer.feature_main.presenter.models.toDivisionForUi
import com.tierriapps.myworkoutorganizer.feature_main.utils.getDivisionByChar
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class EditTrainingViewModel @Inject constructor(
    private val getActualWorkout: GetActualWorkout,
    private  val putDivisionTrainedInWorkout: PutDivisionTrainedInWorkout,
    private val saveWorkout: SaveWorkout,
) : ViewModel() {
    private var actualWorkout: Workout? = null

    private val _divisionStatus = MutableLiveData<DivisionForUi?>()
    val divisionStatus: LiveData<DivisionForUi?> = _divisionStatus

    private val _jobStatus = MutableLiveData<UiText?>()
    val jobStatus: LiveData<UiText?> = _jobStatus

    private var position = 0

    fun getActualDivisionToDo(trainingPosition: Int){
        position = trainingPosition
        viewModelScope.launch {
            val result = getActualWorkout.invoke().last()
            _jobStatus.value = result.message
            actualWorkout = result.content
            if (actualWorkout == null){return@launch}
            _divisionStatus.value = actualWorkout!!.trainingsDone[trainingPosition].toDivisionForUi()
        }
    }

    fun saveTraining(){
        _jobStatus.value = null
        val divisionForUi = _divisionStatus.value
        if (divisionForUi == null || actualWorkout == null){
            _divisionStatus.value = DivisionForUi('A', "error", status = false)
        }else {
            val divisionToSave = Division(
                name = getDivisionByChar(divisionForUi.name),
                description = divisionForUi.description,
            )
            for (exercise in divisionForUi.exercises){
                val listOfReps = mutableListOf<Reps>()
                for (reps in exercise.repsDone){
                    listOfReps.add(Reps(reps))
                }
                val exerciseToSave = Exercise(
                    name = exercise.name?:"",
                    description = exercise.description?:"",
                    numOfSeries = exercise.numOfSeries?:0,
                    timeOfRest = exercise.timeOfRest?:0,
                    type = exercise.type,
                    weight = exercise.weight?:0,
                    seriesDone = listOfReps,
                    image = exercise.image?:""
                )
                divisionToSave.exercises.add(exerciseToSave)
            }
            saveTrainingCreated(divisionToSave)
        }
    }
    private fun saveTrainingCreated(division: Division){
        val updatedWorkout = putDivisionTrainedInWorkout.invoke(actualWorkout!!, division, position)
        viewModelScope.launch {
            saveWorkout.invoke(updatedWorkout).onEach {
                when(it){
                    is Resource.Loading -> _jobStatus.value = null
                    is Resource.Success -> _jobStatus.value = UiText.StringDynamic(
                        "Training Saved!")
                    is Resource.Error -> _jobStatus.value = it.message
                }
            }.collect()
        }
    }

    fun deleteTraining(){
        viewModelScope.launch {
            actualWorkout!!.trainingsDone.removeAt(position)
            saveWorkout.invoke(actualWorkout!!).onEach {
                when(it){
                    is Resource.Loading -> _jobStatus.value = null
                    is Resource.Success -> _jobStatus.value = UiText.StringDynamic(
                        "Training Saved!")
                    is Resource.Error -> _jobStatus.value = it.message
                }
            }.collect()
        }
    }
}