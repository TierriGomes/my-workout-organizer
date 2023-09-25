package com.tierriapps.myworkoutorganizer.feature_main.data.repositories

import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.tierriapps.myworkoutorganizer.common.values.Constants
import com.tierriapps.myworkoutorganizer.feature_main.domain.models.User
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    val firebaseAuth: FirebaseAuth
){
    private val userCollection = FirebaseFirestore.getInstance().collection("users")
    suspend fun changeEmail(email: String, pass: String){
        if (!reauthenticate(pass)){return}
        userCollection.document(Constants.USER_ID).set({"email" to email}).await()
        firebaseAuth.currentUser?.updateEmail(email)
    }
    suspend fun changeName(name: String, pass: String){
        if (!reauthenticate(pass)){return}
        userCollection.document(Constants.USER_ID).set({"name" to name}).await()
    }

    suspend fun getUser(): User {
       val user: User = userCollection.document(Constants.USER_ID).get().await().toObject(User::class.java) as User
        return user
    }

    suspend fun deleteMyAccount(pass: String){
        if (reauthenticate(pass)){
            firebaseAuth.currentUser?.delete()
            userCollection.document(Constants.USER_ID).delete()
        }
    }

    private suspend fun reauthenticate(pass: String): Boolean{
        var toReturn = false
        val auth = EmailAuthProvider.getCredential(firebaseAuth.currentUser?.email.toString(), pass)
        firebaseAuth.currentUser?.reauthenticate(auth)?.addOnCompleteListener {
            if (it.isSuccessful){
                toReturn = true
            }
        }?.await()
        return toReturn
    }
}