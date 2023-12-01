package com.tierriapps.myworkoutorganizer.feature_main.data.local.datasources

import androidx.room.*
import com.tierriapps.myworkoutorganizer.feature_main.data.local.dto.WorkoutRoomEntity

@Dao
interface WorkoutLocalDAO {

    @Query("SELECT * FROM workouts WHERE id = :id")
    suspend fun getWorkoutEntityByID(id: Int): WorkoutRoomEntity?

    @Query("SELECT * FROM workouts")
    suspend fun getAllWorkoutEntities(): List<WorkoutRoomEntity>

    @Delete
    suspend fun deleteWorkoutEntity(workoutRoomEntity: WorkoutRoomEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWorkoutEntity(workoutRoomEntity: WorkoutRoomEntity)

    @Query("DELETE FROM workouts")
    suspend fun deleteAll()
}