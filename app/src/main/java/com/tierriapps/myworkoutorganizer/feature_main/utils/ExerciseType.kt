package com.tierriapps.myworkoutorganizer.feature_main.utils

enum class ExerciseType {
    NORMAL,
    ISOMETRIC,
    DROP_SET,
    UNILATERAL,
    EXPLOSIVE;
    fun getPosition(): Int{
        return values().indexOf(this)
    }


}