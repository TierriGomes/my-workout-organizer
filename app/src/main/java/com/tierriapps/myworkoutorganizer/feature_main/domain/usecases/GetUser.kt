package com.tierriapps.myworkoutorganizer.feature_main.domain.usecases

import com.tierriapps.myworkoutorganizer.feature_main.data.repositories.UserRepositoryImpl
import com.tierriapps.myworkoutorganizer.feature_main.domain.models.User
import javax.inject.Inject

class GetUser @Inject constructor(
    private val userRepositoryImpl: UserRepositoryImpl
) {
    suspend operator fun invoke(): User {
        return userRepositoryImpl.getUser()
    }
}