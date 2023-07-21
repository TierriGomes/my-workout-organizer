package com.tierriapps.myworkoutorganizer.common.utils

import android.content.Context

sealed class UiText{
    data class StringDynamic(val value: String): UiText()
    class StringResource(val resID: Int, vararg val args: Any): UiText()

    fun asString(context: Context): String{
        return when(this){
            is StringDynamic -> value
            is StringResource -> context.getString(resID, *args)
        }
    }

}