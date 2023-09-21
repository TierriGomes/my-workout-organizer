package com.tierriapps.myworkoutorganizer.feature_authentication.domain.repositories

import com.tierriapps.myworkoutorganizer.common.utils.Resource
import kotlinx.coroutines.flow.Flow


interface AuthenticationRepository {

    suspend fun loginWithEmailAndPass(email: String, pass: String): Flow<Resource<String?>>

    suspend fun registerWithEmailAndPass(email: String, pass: String, name: String): Flow<Resource<String?>>

    suspend fun getConnectedUserID(): String?
}