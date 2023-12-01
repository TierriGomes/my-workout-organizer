package com.tierriapps.myworkoutorganizer.feature_main.domain.usecases

import com.tierriapps.myworkoutorganizer.feature_main.data.local.datasources.LocalUserPreferences
import com.tierriapps.myworkoutorganizer.feature_main.data.local.datasources.WorkoutLocalDAO
import javax.inject.Inject

class DeleteLocalData @Inject constructor(
    private val localStorage: WorkoutLocalDAO,
    private val preferences: LocalUserPreferences
) {
    suspend operator fun invoke(){
        localStorage.deleteAll()
        preferences.clearUserPreferences()
    }
}