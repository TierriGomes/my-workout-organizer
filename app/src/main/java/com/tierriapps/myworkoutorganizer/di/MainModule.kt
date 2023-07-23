package com.tierriapps.myworkoutorganizer.di

import android.app.Application
import androidx.datastore.preferences.preferencesDataStore
import androidx.room.Room
import com.google.firebase.firestore.FirebaseFirestore
import com.tierriapps.myworkoutorganizer.common.values.Constants
import com.tierriapps.myworkoutorganizer.feature_main.data.local.datasources.AppLocalDatabase
import com.tierriapps.myworkoutorganizer.feature_main.data.local.datasources.LocalUserPreferences
import com.tierriapps.myworkoutorganizer.feature_main.data.local.datasources.WorkoutLocalDAO
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MainModule {

    @Provides
    @Singleton
    fun providesLocalDataBase(context: Application): AppLocalDatabase{
        return Room.databaseBuilder(
            context,
            AppLocalDatabase::class.java,
            Constants.DATABASE_NAME)
            .build()
    }

    @Provides
    @Singleton
    fun providesWorkoutLocalDAO(appLocalDatabase: AppLocalDatabase): WorkoutLocalDAO{
        return appLocalDatabase.workoutLocalDao()
    }

    @Provides
    @Singleton
    fun providesWorkoutApiService(): FirebaseFirestore{
        return FirebaseFirestore.getInstance()
    }

    @Provides
    @Singleton
    fun providesLocalUserPreferences(context: Application): LocalUserPreferences {
        return LocalUserPreferences(context)
    }
}