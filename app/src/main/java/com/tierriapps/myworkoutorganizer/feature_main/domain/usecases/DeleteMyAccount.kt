package com.tierriapps.myworkoutorganizer.feature_main.domain.usecases

import com.tierriapps.myworkoutorganizer.feature_main.data.repositories.UserRepositoryImpl
import javax.inject.Inject

class DeleteMyAccount @Inject constructor(
    private val userRepositoryImpl: UserRepositoryImpl
) {
    suspend operator fun invoke(pass: String): String{
        return userRepositoryImpl.deleteMyAccount(pass)
    }
}