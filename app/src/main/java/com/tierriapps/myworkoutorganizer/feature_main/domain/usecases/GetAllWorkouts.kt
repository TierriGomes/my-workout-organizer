package com.tierriapps.myworkoutorganizer.feature_main.domain.usecases


import com.tierriapps.myworkoutorganizer.feature_main.domain.repositories.WorkoutRepository
import javax.inject.Inject

class GetAllWorkouts @Inject constructor(private val repository: WorkoutRepository) {
    operator fun invoke() = repository.getAllWorkouts()
}