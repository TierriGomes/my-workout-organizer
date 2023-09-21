package com.tierriapps.myworkoutorganizer.feature_authentication.presenter

import com.tierriapps.myworkoutorganizer.common.utils.UiText

sealed class AuthEvents<T>(val content: T? = null, val message: UiText? = null){
    class NavigateToMainAuthenticated(userID: String): AuthEvents<String>(userID)
    class ErrorSnackBar<T>(content: T? = null, message: UiText?): AuthEvents<T>(content, message)
    class SuccessSnackBar<T>(content: T? = null, message: UiText?): AuthEvents<T>(content, message)
}