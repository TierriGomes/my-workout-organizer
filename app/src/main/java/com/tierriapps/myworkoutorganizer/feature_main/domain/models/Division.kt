package com.tierriapps.myworkoutorganizer.feature_main.domain.models

import com.tierriapps.myworkoutorganizer.feature_main.utils.DivisionName

data class Division (
    val name: DivisionName,
    var description: String = "",
    val exercises: MutableList<Exercise> = mutableListOf()
        )