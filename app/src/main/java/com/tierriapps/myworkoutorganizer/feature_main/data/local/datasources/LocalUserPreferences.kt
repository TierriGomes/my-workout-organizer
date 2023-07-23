package com.tierriapps.myworkoutorganizer.feature_main.data.local.datasources

import android.content.Context
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
            id = it.get(key)
            true
        }
        return id
    }

    suspend fun setActualWorkoutId(int: Int) {
        val key = intPreferencesKey(Constants.ACTUAL_WORKOUT_KEY)
        context.preferences.edit {
            it.set(key, int)
        }
    }

    suspend fun clearUserPreferences() {
        context.preferences.edit {
            it.clear()
        }
    }
}
