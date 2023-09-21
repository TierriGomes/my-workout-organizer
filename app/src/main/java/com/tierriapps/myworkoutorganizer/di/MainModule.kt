package com.tierriapps.myworkoutorganizer.di

import android.app.Application
import androidx.datastore.preferences.preferencesDataStore
import androidx.room.Room
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.tierriapps.myworkoutorganizer.common.values.Constants
import com.tierriapps.myworkoutorganizer.feature_authentication.data.remote.AuthenticationRepositoryImpl
import com.tierriapps.myworkoutorganizer.feature_authentication.domain.repositories.AuthenticationRepository
import com.tierriapps.myworkoutorganizer.feature_main.data.local.datasources.AppLocalDatabase
import com.tierriapps.myworkoutorganizer.feature_main.data.local.datasources.LocalUserPreferences
import com.tierriapps.myworkoutorganizer.feature_main.data.local.datasources.WorkoutLocalDAO
import com.tierriapps.myworkoutorganizer.feature_main.data.remote.datasources.WorkoutRemoteDAO
import com.tierriapps.myworkoutorganizer.feature_main.data.repositories.WorkoutRepositoryImpl
import com.tierriapps.myworkoutorganizer.feature_main.domain.repositories.WorkoutRepository
import com.tierriapps.myworkoutorganizer.feature_main.domain.usecases.CreateDivision
import com.tierriapps.myworkoutorganizer.feature_main.domain.usecases.CreateExercise
import com.tierriapps.myworkoutorganizer.feature_main.domain.usecases.CreateWorkout
import com.tierriapps.myworkoutorganizer.feature_main.domain.usecases.PutDivisionTrainedInWorkout
import com.tierriapps.myworkoutorganizer.feature_main.domain.usecases.PutExerciseInDivision
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
    @Provides
    fun providesCreateExercise(): CreateExercise{
        return CreateExercise()
    }

    @Provides
    fun providesCreateDivision(): CreateDivision {
        return CreateDivision()
    }

    @Provides
    fun providesCreateWorkout(): CreateWorkout {
        return CreateWorkout()
    }
    @Provides
    fun putExerciseInDivision(): PutExerciseInDivision {
        return PutExerciseInDivision()
    }

    @Provides
    @Singleton
    fun providesWorkoutRepository(
        localDAO: WorkoutLocalDAO, remoteDAO: WorkoutRemoteDAO, preferences: LocalUserPreferences)
    : WorkoutRepository {
        return WorkoutRepositoryImpl(remoteDAO, localDAO, preferences)
    }

    @Provides
    @Singleton
    fun providesPutDivisionInWorkout(): PutDivisionTrainedInWorkout{
        return PutDivisionTrainedInWorkout()
    }

    @Provides
    fun provideAuthenticationRepository(): AuthenticationRepository {
        return AuthenticationRepositoryImpl(FirebaseAuth.getInstance(), FirebaseFirestore.getInstance())
    }
}