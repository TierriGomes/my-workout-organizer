package com.tierriapps.myworkoutorganizer.common.utils

fun <T> List<T>.containsSameReference(element: T): Boolean {
    for (item in this) {
        if (item === element) {
            return true
        }
    }
    return false
}