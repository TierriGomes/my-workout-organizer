package com.tierriapps.myworkoutorganizer.common.utils

sealed class Resource<T>(val content: T? = null, val message: UiText? = null){
    class Loading<T>(content: T? = null, message: UiText? = null): Resource<T>(content, message)
    class Success<T>(content: T, message: UiText): Resource<T>(content, message)
    class Error<T>(content: T? = null, message: UiText): Resource<T>(content, message)
}
