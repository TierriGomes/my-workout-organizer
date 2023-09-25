package com.tierriapps.myworkoutorganizer.feature_main.domain.usecases

import com.google.firebase.firestore.FirebaseFirestore
import com.tierriapps.myworkoutorganizer.feature_main.data.repositories.UserRepositoryImpl
import javax.inject.Inject

class ChangeUserName @Inject constructor(private val userRepositoryImpl: UserRepositoryImpl){
    suspend operator fun invoke(name: String, pass: String){
        userRepositoryImpl.changeName(name, pass)
    }
}