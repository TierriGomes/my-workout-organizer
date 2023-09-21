package com.tierriapps.myworkoutorganizer.feature_authentication.presenter.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tierriapps.myworkoutorganizer.common.utils.Resource
import com.tierriapps.myworkoutorganizer.common.utils.UiText
import com.tierriapps.myworkoutorganizer.common.values.Constants
import com.tierriapps.myworkoutorganizer.feature_authentication.domain.use_cases.RegisterUser
import com.tierriapps.myworkoutorganizer.feature_authentication.presenter.AuthEvents
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val registerUseCase: RegisterUser
): ViewModel() {

    private var _resultEvent = MutableLiveData<AuthEvents<String>>()
    val resultEvent: LiveData<AuthEvents<String>> = _resultEvent

    private var _loadingState = MutableLiveData<Boolean>(false)
    val loadingState: LiveData<Boolean> = _loadingState


    fun registerUserWithEmailAndPassword(email: String, pass1: String, pass2: String){
        viewModelScope.launch {
            registerUseCase(email, pass1, pass2).onEach {result ->
                when(result){
                    is Resource.Loading -> {
                        _loadingState.value = true
                    }

                    is Resource.Success -> {
                        _loadingState.value = false
                        _resultEvent.value =
                            (AuthEvents.SuccessSnackBar(message = UiText.StringDynamic("Welcome")))
                        delay(1500)
                        _resultEvent.value =
                            (AuthEvents.NavigateToMainAuthenticated(result.content!!))
                        Constants.USER_ID = result.content!!
                    }

                    is Resource.Error -> {
                        _resultEvent.value =
                            (AuthEvents.ErrorSnackBar(message = result.message))
                        _loadingState.value = false
                    }
                }
            }.launchIn(this)
        }
    }
}