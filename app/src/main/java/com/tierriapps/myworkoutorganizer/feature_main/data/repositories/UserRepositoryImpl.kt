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
    suspend fun changeEmail(email: String, pass: String): String{
        try {
            if (!reauthenticate(pass)){
                print("did not auth")
                return "Invalid password"}
            val user = getUser()!!
            user.email = email
            userCollection.document(Constants.USER_ID).set(user).await()
            firebaseAuth.currentUser?.updateEmail(email)
            return "Email changed"
        } catch (e: Exception){
            return e.message.toString()
        }
    }
    suspend fun changeName(name: String, pass: String): String{
        try {
            if (!reauthenticate(pass)){
                print("did noy auth")
                return "Invalid password"
            }
            val user = getUser()!!
            print(user)
            user.name = name
            userCollection.document(Constants.USER_ID).set(user).await()
            return "Name changed"
        } catch (e: Exception){
            return e.message.toString()
        }
    }

    suspend fun getUser(): User? {
        try {
            val user: User = userCollection.document(Constants.USER_ID).get().await().toObject(User::class.java) as User
            return user
        } catch (e: Exception){
            return null
        }
    }

    suspend fun deleteMyAccount(pass: String): String {
        try {
            if (reauthenticate(pass)){
                userCollection.document(Constants.USER_ID).delete().await()
                firebaseAuth.currentUser?.delete()?.await()
                return "User Deleted"
            } else {
                return "Invalid pass"
            }
        } catch (e: Exception){
            return e.message.toString()
        }
    }

    private suspend fun reauthenticate(pass: String): Boolean{
        try {
            val auth = EmailAuthProvider.getCredential(firebaseAuth.currentUser?.email.toString(), pass)
            firebaseAuth.currentUser?.reauthenticate(auth)?.await()
            return true
        }catch (e: Exception){
            return false
        }
    }
}