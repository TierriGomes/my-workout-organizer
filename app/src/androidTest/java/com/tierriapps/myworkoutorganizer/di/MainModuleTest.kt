package com.tierriapps.myworkoutorganizer.di

import android.app.Application
import androidx.room.Room
import com.google.firebase.firestore.FirebaseFirestore
import com.tierriapps.myworkoutorganizer.feature_main.data.local.datasources.AppLocalDatabase
import com.tierriapps.myworkoutorganizer.feature_main.data.local.datasources.WorkoutLocalDAO
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [MainModule::class]
)
object MainModuleTest {
    @Provides
    fun providesLocalDataBase(context: Application): AppLocalDatabase {
        return Room.inMemoryDatabaseBuilder(
            context,
            AppLocalDatabase::class.java)
            .allowMainThreadQueries()
            .build()
    }

    @Provides
    fun providesWorkoutLocalDAO(appLocalDatabase: AppLocalDatabase): WorkoutLocalDAO {
        return appLocalDatabase.workoutLocalDao()
    }

    @Provides
    fun providesWorkoutApiService(): FirebaseFirestore {
        return FirebaseFirestore.getInstance()
    }
}