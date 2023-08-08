package com.tierriapps.myworkoutorganizer.feature_main.presenter.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tierriapps.myworkoutorganizer.feature_main.presenter.models.ExerciseForCreateWorkout


class TestViewModel: ViewModel() {
    private val _listOfExercises = MutableLiveData<MutableList<ExerciseForCreateWorkout>>(
        mutableListOf()
    )
    val listOfExercises: LiveData<MutableList<ExerciseForCreateWorkout>> = _listOfExercises
    fun printItems(){
        println(_listOfExercises.value.toString())
    }
}