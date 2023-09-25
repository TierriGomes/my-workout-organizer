package com.tierriapps.myworkoutorganizer.feature_authentication.presenter.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tierriapps.myworkoutorganizer.feature_authentication.domain.use_cases.ResetPasswordUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ResetPasswordViewModel @Inject constructor(
    private val resetPasswordUseCase: ResetPasswordUseCase
): ViewModel(){
    private val _result = MutableLiveData<String>("")
    val result = _result as LiveData<String>
    fun orderReset(email: String){
        viewModelScope.launch {
            val wait = resetPasswordUseCase.invoke(email)
            _result.value = wait.content?:"Error, try again later"
        }
    }
}