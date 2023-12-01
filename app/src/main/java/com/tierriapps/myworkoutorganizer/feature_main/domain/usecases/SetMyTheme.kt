package com.tierriapps.myworkoutorganizer.feature_main.domain.usecases

import com.tierriapps.myworkoutorganizer.feature_main.data.repositories.PreferencesRepositoryImpl
import javax.inject.Inject

class SetMyTheme @Inject constructor(
    private val preferencesRepositoryImpl: PreferencesRepositoryImpl
) {
    // true if is light false if is dark
    suspend operator fun invoke(boolean: Boolean){
        preferencesRepositoryImpl.setMyTheme(boolean)
    }
}