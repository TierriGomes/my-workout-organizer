package com.tierriapps.myworkoutorganizer.common.utils

fun <T> List<T>.containsSameReference(element: T): Boolean {
    for (item in this) {
        if (item === element) {
            return true
        }
    }
    return false
}

fun String.getNumberList(): List<Int>{
    val list = mutableListOf<Int>()
    var number = ""
    var canAdd = true
    for (char in this){
        try {
            char.toString().toInt()
            number += char
        }catch (e: Exception){
            if (number.toIntOrNull() != null){
                number = ""
                canAdd = true
            }
        }finally {
            if (number.toIntOrNull() != null){
                if (canAdd){
                    list.add(number.toInt())
                    canAdd = false
                }else {
                    list[list.lastIndex] = number.toInt()
                }
            }
        }
    }
    return list
}