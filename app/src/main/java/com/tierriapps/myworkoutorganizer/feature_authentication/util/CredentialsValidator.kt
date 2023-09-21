package com.tierriapps.myworkoutorganizer.utils

import android.util.Log

fun validateEmail(email: String): Boolean {
    println(email)
    var validChars = "#&_-0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz."
    if (email.count { it == '@' } != 1 || email.count() !in 8..32) {
        return false
    }

    val emailBeforeA = email.filterIndexed {index, char -> index < email.indexOf('@') }
    println("validateEmail1" + emailBeforeA + "    -> ${emailBeforeA.length}")
    for (c in emailBeforeA){
        if (c !in validChars && c != '.'){
            return false
        }
    }

    val emailAfterA = email.replace("$emailBeforeA@", "")
    println("validateEmail2" + emailAfterA)
    if (emailAfterA.count { it == '.' } < 1
        || emailAfterA.last() == '.'
        || emailAfterA.first() == '.'){
        return false
    }
    validChars = validChars.replace("#&","")
    println("validateEmail3" + validChars.toString())
    for (c in emailAfterA){
        if (c !in validChars && c != '.') { return false }
        if (c == '.') {
            validChars = validChars.replace("_-0123456789", "")
            ///emailAfterA.replace("..", "!!")
        }
    }
    return true
}

fun validatePassword(password: String): Boolean {
    var containChar = false
    var containNum = false
    if (password.count() in 8..32){
        for(c in password){
            when(c){
                in "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz" -> containChar = true
                in "1234567890" -> containNum = true
            }
            if (containChar && containNum){
                return true
            }
        }
    }
    return false
}