package com.tierriapps.myworkoutorganizer.feature_main.presenter.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tierriapps.myworkoutorganizer.feature_main.domain.models.User
import com.tierriapps.myworkoutorganizer.feature_main.domain.usecases.DeleteLocalData
import com.tierriapps.myworkoutorganizer.feature_main.domain.usecases.DeleteWorkout
import com.tierriapps.myworkoutorganizer.feature_main.domain.usecases.GetAllWorkouts
import com.tierriapps.myworkoutorganizer.feature_main.domain.usecases.GetMyTheme
import com.tierriapps.myworkoutorganizer.feature_main.domain.usecases.GetUser
import com.tierriapps.myworkoutorganizer.feature_main.domain.usecases.SetMyTheme
import com.tierriapps.myworkoutorganizer.feature_main.domain.usecases.SynchronizeData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StartViewModel @Inject constructor(
    private val deleteWorkout: DeleteWorkout,
    private val getAllWorkouts: GetAllWorkouts,
    private val getUser: GetUser,
    private val getMyTheme: GetMyTheme,
    private val setMyTheme: SetMyTheme,
    private val synchronizeData: SynchronizeData,
    private val deleteLocalData: DeleteLocalData
): ViewModel() {
    var theme = MutableLiveData<Boolean>(true)
    val userName = MutableLiveData<String>()
    init {
        viewModelScope.launch {
            synchronizeData()
            val myUser = getUser.invoke()
            userName.value = myUser?.name?:""
        }
    }
    // true for light and false for dark
    fun saveTheme(boolean: Boolean){
        theme.value = boolean
        viewModelScope.launch {
            setMyTheme.invoke(boolean)
        }
    }

    fun getTheme(){
        viewModelScope.launch {
            theme.value = getMyTheme.invoke()
        }
    }

    fun synchronizeData(){
        viewModelScope.launch {
            synchronizeData.invoke()
        }
    }

    fun deleteAllWorkouts(){
        viewModelScope.launch {
            try {
                val workouts = getAllWorkouts.invoke().last()!!.content!!
                for (w in workouts){
                    deleteWorkout.invoke(w)
                }
            }catch (e: Exception){ //ok
             }
        }
    }

    fun deleteLocalData(){
        viewModelScope.launch {
            deleteLocalData.invoke()
        }
    }
}