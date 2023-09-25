package com.tierriapps.myworkoutorganizer.feature_main.domain.usecases

import com.tierriapps.myworkoutorganizer.feature_main.data.repositories.UserRepositoryImpl
import com.tierriapps.myworkoutorganizer.utils.validateEmail
import javax.inject.Inject

class ChangeUserEmail @Inject constructor(
    private val userRepositoryImpl: UserRepositoryImpl
) {
    suspend operator fun invoke(email: String, pass: String){
        if (validateEmail(email)){
            userRepositoryImpl.changeEmail(email, pass)
        }
    }
}