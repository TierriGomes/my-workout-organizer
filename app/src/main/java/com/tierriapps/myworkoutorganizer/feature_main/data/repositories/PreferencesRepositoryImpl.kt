package com.tierriapps.myworkoutorganizer.feature_main.data.repositories

import com.tierriapps.myworkoutorganizer.feature_main.data.local.datasources.LocalUserPreferences
import javax.inject.Inject

class PreferencesRepositoryImpl @Inject constructor(
    private val localUserPreferences: LocalUserPreferences
) {
    suspend fun getMyTheme(): Boolean{
        return localUserPreferences.getTheme()
    }

    suspend fun setMyTheme(booleanTheme: Boolean){
        return localUserPreferences.setTheme(booleanTheme)
    }

    suspend fun isTherePendingTasks(): Boolean{
        return localUserPreferences.isTherePendingTasksInRemote()
    }

    suspend fun removePendingTasks(){
        localUserPreferences.setPendingTasksInRemote(false)
    }
}