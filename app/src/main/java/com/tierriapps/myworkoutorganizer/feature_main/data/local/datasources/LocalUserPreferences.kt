package com.tierriapps.myworkoutorganizer.feature_main.data.local.datasources

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.tierriapps.myworkoutorganizer.common.values.Constants
import kotlinx.coroutines.flow.first
import javax.inject.Inject


private val Context.preferences by preferencesDataStore(Constants.PREFERENCES_NAME)
class LocalUserPreferences @Inject constructor(
    private val context: Context,
) {
    suspend fun getActualWorkoutId(): Int? {
        val key = intPreferencesKey(Constants.ACTUAL_WORKOUT_KEY)
        var id: Int? = null
        context.preferences.data.first {
            id = it[key]
            true
        }
        return id
    }

    suspend fun setActualWorkoutId(int: Int) {
        val key = intPreferencesKey(Constants.ACTUAL_WORKOUT_KEY)
        context.preferences.edit {
            it[key] = int
        }
    }

    suspend fun setPendingTasksInRemote(isThere: Boolean) {
        val key = booleanPreferencesKey(Constants.PENDING_WORKOUTS_KEY)
        context.preferences.edit {
            it[key] = isThere
        }
    }

    suspend fun isTherePendingTasksInRemote(): Boolean {
        var value = false
        val key = booleanPreferencesKey(Constants.PENDING_WORKOUTS_KEY)
        context.preferences.data.first {
            value = it[key]?:false
            true
        }
        return value
    }

    suspend fun setTheme(boolean: Boolean){
        val key = booleanPreferencesKey(Constants.THEME_KEY)
        context.preferences.edit {
            it[key] = boolean
        }
    }

    suspend fun getTheme(): Boolean {
        var value = true
        val key = booleanPreferencesKey(Constants.THEME_KEY)
        context.preferences.data.first {
            value = it[key]?:true
            true
        }
        return value
    }

    suspend fun clearUserPreferences() {
        context.preferences.edit {
            it.clear()
        }
    }
}
