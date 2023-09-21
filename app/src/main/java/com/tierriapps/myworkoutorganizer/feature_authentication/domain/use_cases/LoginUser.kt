package com.tierriapps.myworkoutorganizer.feature_authentication.domain.use_cases

import com.tierriapps.myworkoutorganizer.R
import com.tierriapps.myworkoutorganizer.common.utils.Resource
import com.tierriapps.myworkoutorganizer.common.utils.UiText
import com.tierriapps.myworkoutorganizer.feature_authentication.domain.repositories.AuthenticationRepository
import com.tierriapps.myworkoutorganizer.utils.validateEmail
import com.tierriapps.myworkoutorganizer.utils.validatePassword
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject


class LoginUser @Inject constructor (private val repository: AuthenticationRepository){
    suspend operator fun invoke(email: String, pass: String): Flow<Resource<String?>> {
        val emailValidation = validateEmail(email)
        val passValidation = validatePassword(pass)
        return if (emailValidation && passValidation){
            repository.loginWithEmailAndPass(email, pass)
        } else if(!emailValidation){
            flow { emit(Resource.Error(null, UiText.StringResource(R.string.invalid_email))) }
        } else{
            flow { emit(Resource.Error(null, UiText.StringResource(R.string.invalid_password))) }
        }
    }
}