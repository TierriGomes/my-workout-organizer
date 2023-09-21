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


class RegisterUser @Inject constructor(
    private val repository: AuthenticationRepository
) {
    suspend operator fun invoke(email: String, password1: String, password2: String): Flow<Resource<String?>> {
        if (!validateEmail(email)){
            return flow { emit(Resource.Error(null, UiText.StringResource(R.string.invalid_email))) }
        }else if(!validatePassword(password1)){
            return flow { emit(Resource.Error(null, UiText.StringResource(R.string.invalid_password))) }
        }else if(password1 != password2){
            return flow { emit(Resource.Error(null, UiText.StringResource(R.string.different_passwords))) }
        }else{
            return repository.registerWithEmailAndPass(email, password1)
        }
    }
}