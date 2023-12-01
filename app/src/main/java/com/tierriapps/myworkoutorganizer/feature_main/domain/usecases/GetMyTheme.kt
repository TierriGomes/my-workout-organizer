package com.tierriapps.myworkoutorganizer.feature_main.domain.usecases

import com.tierriapps.myworkoutorganizer.feature_main.data.repositories.PreferencesRepositoryImpl
import javax.inject.Inject

class GetMyTheme @Inject constructor(
    private val preferencesRepositoryImpl: PreferencesRepositoryImpl
) {
    suspend operator fun invoke(): Boolean{
        return preferencesRepositoryImpl.getMyTheme()
    }
}