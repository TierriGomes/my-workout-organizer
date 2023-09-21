package com.tierriapps.myworkoutorganizer.feature_authentication.domain.use_cases

import com.tierriapps.myworkoutorganizer.feature_authentication.domain.repositories.AuthenticationRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject


class GetUserIfLogged @Inject constructor(val authRepository: AuthenticationRepository) {
    suspend operator fun invoke() = withContext(Dispatchers.IO){
        return@withContext authRepository.getConnectedUserID()
    }
}