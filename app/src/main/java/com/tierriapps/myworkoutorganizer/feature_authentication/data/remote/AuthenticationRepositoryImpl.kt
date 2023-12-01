package com.tierriapps.myworkoutorganizer.feature_authentication.data.remote

import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.firestore.FirebaseFirestore
import com.tierriapps.myworkoutorganizer.R
import com.tierriapps.myworkoutorganizer.common.utils.Resource
import com.tierriapps.myworkoutorganizer.common.utils.UiText
import com.tierriapps.myworkoutorganizer.common.values.Constants
import com.tierriapps.myworkoutorganizer.feature_authentication.domain.repositories.AuthenticationRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthenticationRepositoryImpl @Inject constructor(
    private val userBase: FirebaseAuth,
    private val dataBase: FirebaseFirestore
): AuthenticationRepository {
    override suspend fun loginWithEmailAndPass(email: String, pass: String): Flow<Resource<String?>> = flow {
        emit(Resource.Loading<String?>())
        try {
            userBase.signInWithEmailAndPassword(email, pass).await()
            emit(Resource.Success(userBase.uid, UiText.StringResource(R.string.logged_with_succes)))
        } catch (e: FirebaseNetworkException) {
            emit(Resource.Error(null, UiText.StringResource(R.string.connection_error)))
        } catch (e: FirebaseAuthInvalidCredentialsException) {
            emit(Resource.Error(null, UiText.StringResource(R.string.invalid_credentials)))
        } catch (e: FirebaseAuthInvalidUserException) {
            emit(Resource.Error(null, UiText.StringResource(R.string.user_does_not_exist)))
        } catch (e: Exception) {
            emit(Resource.Error(null, UiText.StringResource(R.string.error)))
        }
    }

    override suspend fun registerWithEmailAndPass(email: String, pass: String, name: String): Flow<Resource<String?>> = flow {
        emit(Resource.Loading<String?>())
        try {
            userBase.createUserWithEmailAndPassword(email, pass).await()
            val userData = mapOf<String, String>(
                "name" to name,
                "email" to email
            )
            dataBase.collection(Constants.USER_REMOTE_COLLECTION)
                .document(userBase.uid.toString()).set(userData).await()
            emit(Resource.Success(userBase.uid, UiText.StringResource(R.string.registred_with_succes)))
        } catch (e: FirebaseNetworkException) {
            emit(Resource.Error(null, UiText.StringResource(R.string.connection_error)))
        } catch (e: FirebaseAuthUserCollisionException) {
            emit(Resource.Error(null, UiText.StringResource(R.string.email_already_registred)))
        } catch (e: FirebaseAuthInvalidCredentialsException) {
            emit(Resource.Error( null, UiText.StringResource(R.string.invalid_credentials)))
        } catch (e: Exception) {
            emit(Resource.Error(null, UiText.StringResource(R.string.error)))
        }
    }

    override suspend fun getConnectedUserID() = userBase.uid
}