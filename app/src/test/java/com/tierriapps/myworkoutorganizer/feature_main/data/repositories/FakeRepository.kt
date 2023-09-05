package com.tierriapps.myworkoutorganizer.feature_main.data.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tierriapps.myworkoutorganizer.R
import com.tierriapps.myworkoutorganizer.common.utils.Resource
import com.tierriapps.myworkoutorganizer.common.utils.UiText
import com.tierriapps.myworkoutorganizer.feature_main.domain.models.Division
import com.tierriapps.myworkoutorganizer.feature_main.domain.models.Exercise
import com.tierriapps.myworkoutorganizer.feature_main.domain.models.Reps
import com.tierriapps.myworkoutorganizer.feature_main.domain.models.Workout
import com.tierriapps.myworkoutorganizer.feature_main.domain.repositories.WorkoutRepository
import com.tierriapps.myworkoutorganizer.feature_main.utils.DivisionName
import com.tierriapps.myworkoutorganizer.feature_main.utils.ExerciseType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeRepository: WorkoutRepository {
    var shouldReturnSuccess = true
    var listOfWorkouts = listOf(
        Workout(
            1, "name1", "description",
            divisions = listOf<Division>(
                Division(
                    name = DivisionName.A,
                    description = "Esta é a descrição da Divisão 1",
                    exercises = mutableListOf(
                        Exercise(
                            name = "Exercício A1",
                            description = "Descrição do Exercício 1",
                            numOfSeries = 3,
                            timeOfRest = 60,
                            type = ExerciseType.NORMAL,
                            weight = 50,
                            image = "imagem_exercicio1.jpg"
                        ),
                        Exercise(
                            name = "Exercício A2",
                            description = "Descrição do Exercício 2",
                            numOfSeries = 4,
                            timeOfRest = 45,
                            type = ExerciseType.NORMAL,
                            weight = 40,
                            image = "imagem_exercicio2.jpg"
                        )
                    )
                ),
                Division(
                    name = DivisionName.B,
                    description = "Esta é a descrição da Divisão 2",
                    exercises = mutableListOf(
                        Exercise(
                            name = "Exercício B1",
                            description = "Descrição do Exercício 3",
                            numOfSeries = 3,
                            timeOfRest = 60,
                            type = ExerciseType.NORMAL,
                            weight = 60,
                            image = "imagem_exercicio3.jpg"
                        ),
                        Exercise(
                            name = "Exercício B2",
                            description = "Descrição do Exercício 4",
                            numOfSeries = 5,
                            timeOfRest = 45,
                            type = ExerciseType.NORMAL,
                            weight = 45,
                            image = "imagem_exercicio4.jpg"
                        )
                    )
                )
            ),
            trainingsDone = mutableListOf<Division>(
                Division(
                    name = DivisionName.A,
                    description = "Esta é a descrição da Divisão 1",
                    exercises = mutableListOf(
                        Exercise(
                            name = "Exercício A1",
                            description = "Descrição do Exercício 1",
                            numOfSeries = 3,
                            timeOfRest = 60,
                            type = ExerciseType.NORMAL,
                            weight = 50,
                            seriesDone = mutableListOf(
                                Reps(listOf(10, 10, 8)),
                                Reps(listOf(12, 12, 10)),
                                Reps(listOf(10, 10, 8))),
                            image = "imagem_exercicio1.jpg"
                            ),
                            Exercise(
                                name = "Exercício A2",
                                description = "Descrição do Exercício 2",
                                numOfSeries = 4,
                                timeOfRest = 45,
                                type = ExerciseType.NORMAL,
                                weight = 40,
                                seriesDone = mutableListOf(
                                    Reps(listOf(8, 8 )),
                                    Reps(listOf(10, 10, 8)),
                                    Reps(listOf(8, 8 )),
                                    Reps(listOf(10, 10, 8)),
                                ),
                                image = "imagem_exercicio2.jpg"
                            )
                        )
                    ),
                Division(
                    name = DivisionName.B,
                    description = "Esta é a descrição da Divisão 2",
                    exercises = mutableListOf(
                        Exercise(
                            name = "Exercício B1",
                            description = "Descrição do Exercício 3",
                            numOfSeries = 3,
                            timeOfRest = 60,
                            type = ExerciseType.NORMAL,
                            weight = 60,
                            seriesDone = mutableListOf(
                                Reps(listOf(8, 8, 6)),
                                Reps(listOf(8, 8, 6)),
                                Reps(listOf(10, 10, 8))
                            ),
                            image = "imagem_exercicio3.jpg"
                        ),
                        Exercise(
                            name = "Exercício B2",
                            description = "Descrição do Exercício 4",
                            numOfSeries = 5,
                            timeOfRest = 45,
                            type = ExerciseType.NORMAL,
                            weight = 45,
                            seriesDone = mutableListOf(
                                Reps(listOf(12, 12, 10, 10, 8)),
                                Reps(listOf(15, 15, 12, 12, 10)),
                                Reps(listOf(12, 12, 10, 10, 8)),
                                Reps(listOf(15, 15, 12, 12, 10)),
                                Reps(listOf(12, 12, 10, 10, 8))
                            ),
                            image = "imagem_exercicio4.jpg"
                        )
                    )
                )
            )
        ),
        Workout(
            2, "name2", "description",)
    )

    var getAllWorkoutCalls = 0
    var getWorkoutByIdCalls = 0
    var insertWorkoutCalls = 0
    var deleteWorkoutCalls = 0
    var getActualWorkoutCalls = 0
    var setActualWorkoutCalls = 0
    var getUserIdCalls = 0
    var getUserNameCalls = 0

    override fun getAllWorkouts(): Flow<Resource<List<Workout>>?> = flow {
        getAllWorkoutCalls ++
        emit(Resource.Loading())
        if (shouldReturnSuccess && listOfWorkouts.isNotEmpty()){
            emit(Resource.Success(listOfWorkouts, UiText.StringResource(R.string.success)))
        }
        else {
            emit(Resource.Error(null, UiText.StringResource(R.string.error)))
        }
    }

    override fun getWorkoutById(id: Int): Flow<Resource<Workout?>> = flow {
        getWorkoutByIdCalls ++
        emit(Resource.Loading())
        if (shouldReturnSuccess && listOfWorkouts.isNotEmpty()){
            emit(Resource.Success(listOfWorkouts[id-1], UiText.StringResource(R.string.success)))
        }
        else {
            emit(Resource.Error(null, UiText.StringResource(R.string.error)))
        }
    }

    override fun insertWorkout(workout: Workout): Flow<Resource<Workout?>> = flow {
        insertWorkoutCalls ++
        emit(Resource.Loading())
        if (shouldReturnSuccess){
            emit(Resource.Success(listOfWorkouts[1], UiText.StringResource(R.string.success)))
        }
        else {
            emit(Resource.Error(null, UiText.StringResource(R.string.error)))
        }
    }

    override fun deleteWorkout(workout: Workout): Flow<Resource<Workout?>>  = flow {
        deleteWorkoutCalls ++
        emit(Resource.Loading())
        if (shouldReturnSuccess){
            emit(Resource.Success(listOfWorkouts[1], UiText.StringResource(R.string.success)))
        }
        else {
            emit(Resource.Error(null, UiText.StringResource(R.string.error)))
        }
    }

    override suspend fun getActualWorkoutId(): Resource<Int?> {
        getActualWorkoutCalls ++
        if (shouldReturnSuccess){
            return Resource.Success(1, UiText.StringResource(R.string.success))
        }
        else {
            return Resource.Error(null, UiText.StringResource(R.string.error))
        }
    }

    override suspend fun setActualWorkoutId(id: Int): Resource<Int?> {
        setActualWorkoutCalls ++
        if (shouldReturnSuccess){
            return Resource.Success(1, UiText.StringResource(R.string.success))
        }
        else {
            return Resource.Error(null, UiText.StringResource(R.string.error))
        }
    }

    override fun getUserId(): LiveData<String> {
        getUserIdCalls ++
        return MutableLiveData("userId")
    }

    override fun getUserName(): LiveData<String> {
        getUserNameCalls ++
        return MutableLiveData("userName")
    }
}


    val division2 = Division(
        name = DivisionName.B,
        description = "Esta é a descrição da Divisão 2",
        exercises = mutableListOf(
            Exercise(
                name = "Exercício 3",
                description = "Descrição do Exercício 3",
                numOfSeries = 3,
                timeOfRest = 60,
                type = ExerciseType.NORMAL,
                weight = 60,
                seriesDone = mutableListOf(
                    Reps(listOf(8, 8, 6)),
                    Reps(listOf(10, 10, 8))
                ),
                image = "imagem_exercicio3.jpg"
            ),
            Exercise(
                name = "Exercício 4",
                description = "Descrição do Exercício 4",
                numOfSeries = 5,
                timeOfRest = 45,
                type = ExerciseType.NORMAL,
                weight = 45,
                seriesDone = mutableListOf(
                    Reps(listOf(12, 12, 10, 10, 8)),
                    Reps(listOf(15, 15, 12, 12, 10))
                ),
                image = "imagem_exercicio4.jpg"
            )
        )
    )
