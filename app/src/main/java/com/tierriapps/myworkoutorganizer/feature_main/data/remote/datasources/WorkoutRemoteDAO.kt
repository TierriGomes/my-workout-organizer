package com.tierriapps.myworkoutorganizer.feature_main.data.remote.datasources


import com.google.firebase.firestore.FirebaseFirestore
import com.tierriapps.myworkoutorganizer.common.values.Constants
import com.tierriapps.myworkoutorganizer.feature_main.data.remote.dto.WorkoutRemoteEntity
import kotlinx.coroutines.tasks.await
import javax.inject.Inject


class WorkoutRemoteDAO @Inject constructor(val workoutApiService: FirebaseFirestore) {
    private val collection = workoutApiService.collection(Constants.WORKOUT_REMOTE_COLLECTION)
    suspend fun getWorkoutEntityByID(localId: Int, ownerId: String): WorkoutRemoteEntity? {
        return collection
            .whereEqualTo("ownerId", ownerId)
            .whereEqualTo("localId", localId)
            .get().await().documents[0].toObject(WorkoutRemoteEntity::class.java)
    }

    suspend fun getAllWorkoutEntities(ownerId: String): List<WorkoutRemoteEntity> {
        return collection
            .whereEqualTo("ownerId", ownerId)
            .get().await().toObjects(WorkoutRemoteEntity::class.java).sortedBy {
                it.localId
            }
    }

    suspend fun deleteWorkoutEntity(workoutRemoteEntity: WorkoutRemoteEntity) {
        collection
            .whereEqualTo("ownerId", workoutRemoteEntity.ownerId)
            .whereEqualTo("localId", workoutRemoteEntity.localId)
            .get().await().documents[0].reference.delete().await()
    }

    suspend fun insertWorkoutEntity(workoutRemoteEntity: WorkoutRemoteEntity) {
        val reference = collection
            .whereEqualTo("ownerId", workoutRemoteEntity.ownerId)
            .whereEqualTo("localId", workoutRemoteEntity.localId)
            .get().await()
        if ( reference.documents.isEmpty()){
            collection.add(workoutRemoteEntity).await()
        }else {
            reference.documents[0].reference.set(workoutRemoteEntity).await()
        }

    }
}