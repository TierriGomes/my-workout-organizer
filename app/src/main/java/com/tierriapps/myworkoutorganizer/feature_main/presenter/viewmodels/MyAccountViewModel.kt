package com.tierriapps.myworkoutorganizer.feature_main.presenter.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tierriapps.myworkoutorganizer.feature_main.domain.models.User
import com.tierriapps.myworkoutorganizer.feature_main.domain.usecases.ChangeUserEmail
import com.tierriapps.myworkoutorganizer.feature_main.domain.usecases.ChangeUserName
import com.tierriapps.myworkoutorganizer.feature_main.domain.usecases.DeleteMyAccount
import com.tierriapps.myworkoutorganizer.feature_main.domain.usecases.GetUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyAccountViewModel @Inject constructor(
    private val getUser: GetUser,
    private val changeUserEmail: ChangeUserEmail,
    private val changeUserName: ChangeUserName,
    private val deleteMyAccount: DeleteMyAccount
): ViewModel() {
    private val _user = MutableLiveData<User>()
    val user: LiveData<User> = _user

    fun fetchData(){
        viewModelScope.launch {
            _user.value = getUser.invoke()
        }
    }

    fun changeUserName(name: String, pass: String){
        viewModelScope.launch {
            changeUserName.invoke(name, pass)
        }
    }

    fun changeUserEmail(email: String, pass: String){
        viewModelScope.launch {
            changeUserEmail.invoke(email, pass)
        }
    }

    fun deleteMyAccount(pass: String){
        viewModelScope.launch {
            deleteMyAccount.invoke(pass)
        }
    }
}