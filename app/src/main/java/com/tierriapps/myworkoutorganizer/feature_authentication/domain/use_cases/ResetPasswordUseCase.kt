package com.tierriapps.myworkoutorganizer.feature_authentication.domain.use_cases

import com.google.android.recaptcha.Recaptcha
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.tierriapps.myworkoutorganizer.common.utils.Resource
import com.tierriapps.myworkoutorganizer.common.utils.UiText
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ResetPasswordUseCase @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) {
    suspend operator fun invoke(email: String): Resource<String> {
        val result1 = firestore.collection("users")
            .whereEqualTo("email", email).get().await()
        if (result1.isEmpty){
            return Resource.Error("Email not registered", UiText.StringDynamic("Email not registered"))
        } else  {
            val result2 = firebaseAuth.sendPasswordResetEmail(email)
            var resource: Resource<String>? = null
            resource = Resource.Success("Email send, check your email box", UiText.StringDynamic("Check your email box"))
            result2.addOnFailureListener {
                resource = Resource.Error("Error, try again later", UiText.StringDynamic("Error, try again later"))
            }
            return resource!!
        }
    }
}