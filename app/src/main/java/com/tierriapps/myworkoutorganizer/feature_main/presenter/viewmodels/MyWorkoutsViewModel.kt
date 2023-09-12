package com.tierriapps.myworkoutorganizer.feature_main.presenter.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tierriapps.myworkoutorganizer.common.utils.Resource
import com.tierriapps.myworkoutorganizer.feature_main.domain.models.Workout
import com.tierriapps.myworkoutorganizer.feature_main.domain.usecases.DeleteWorkout
import com.tierriapps.myworkoutorganizer.feature_main.domain.usecases.GetActualWorkout
import com.tierriapps.myworkoutorganizer.feature_main.domain.usecases.GetAllWorkouts
import com.tierriapps.myworkoutorganizer.feature_main.domain.usecases.SetActualWorkout
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyWorkoutsViewModel @Inject constructor(
    private val getAllWorkouts: GetAllWorkouts,
    private val getActualWorkout: GetActualWorkout,
    private val setActualWorkout: SetActualWorkout,
    private val deleteWorkout: DeleteWorkout
): ViewModel() {
    private val _actualWorkout = MutableLiveData<Workout?>()
    val actualWorkout: LiveData<Workout?> = _actualWorkout

    private val _allWorkouts = MutableLiveData<List<Workout>>()
    val allWorkouts: LiveData<List<Workout>> = _allWorkouts

    fun fetchData(){
        viewModelScope.launch {
            val allWorkoutsResult = getAllWorkouts.invoke().last()
            val actualWorkoutResult = getActualWorkout.invoke().last()

            _allWorkouts.value = allWorkoutsResult?.content?: listOf()
            _actualWorkout.value = actualWorkoutResult.content

        }
    }

    fun setActualWorkout(workout: Workout){
        viewModelScope.launch {
            setActualWorkout.invoke(workout).last()
            fetchData()
        }

    }

    fun deleteWorkout(workout: Workout){
        viewModelScope.launch {
            deleteWorkout.invoke(workout)
            fetchData()
        }
    }
}