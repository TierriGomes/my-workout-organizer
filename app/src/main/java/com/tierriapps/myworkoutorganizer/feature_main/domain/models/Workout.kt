package com.tierriapps.myworkoutorganizer.feature_main.domain.models

import com.tierriapps.myworkoutorganizer.feature_main.utils.DivisionName

data class Workout(
    val id: Int? = null,
    val name: String = "",
    val description: String,
    val divisions: List<Division>,
    val trainingsDone: MutableList<Division> = mutableListOf()
    /**
     *
     */
)
