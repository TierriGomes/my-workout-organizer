package com.tierriapps.myworkoutorganizer.feature_main.data.local.datasources

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.tierriapps.myworkoutorganizer.feature_main.data.local.dto.WorkoutRoomEntity
import com.tierriapps.myworkoutorganizer.feature_main.data.local.dto.WorkoutRoomEntityTypeConverter

@Database(entities = [WorkoutRoomEntity::class], version = 1)
@TypeConverters(WorkoutRoomEntityTypeConverter::class)
abstract class AppLocalDatabase: RoomDatabase() {

    abstract fun workoutLocalDao(): WorkoutLocalDAO
}