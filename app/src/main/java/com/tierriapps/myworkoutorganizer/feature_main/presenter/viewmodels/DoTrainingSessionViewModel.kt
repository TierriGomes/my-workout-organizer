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
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DoTrainingSessionViewModel @Inject constructor(
    private val getActualWorkout: GetActualWorkout,
) : ViewModel() {
    private var actualWorkout: Workout? = null

    private val _divisionStatus = MutableLiveData<DivisionForUi?>()
    val divisionStatus: LiveData<DivisionForUi?> = _divisionStatus

    fun getActualDivisionToDo(divisionName: String){
        viewModelScope.launch {
            actualWorkout = getActualWorkout.invoke().last().content
            if (actualWorkout == null){return@launch}
            val division = actualWorkout!!.divisions.find { it.name.char == divisionName[0] }
            _divisionStatus.value = division?.toDivisionForUi()
        }
    }

    fun createAndSaveTraining(){
        
    }
}