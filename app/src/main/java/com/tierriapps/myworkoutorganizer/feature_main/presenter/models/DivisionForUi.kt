package com.tierriapps.myworkoutorganizer.feature_main.presenter.models

import com.tierriapps.myworkoutorganizer.R
import com.tierriapps.myworkoutorganizer.common.utils.UiText
import com.tierriapps.myworkoutorganizer.feature_main.domain.models.Division


class DivisionForUi(
    var name: Char,
    var description: String,
    val exercises: MutableList<ExerciseForUi> = mutableListOf(),
    var status: Boolean = true,
    var message: UiText? = null
    ){

    fun colorForBackGround(): Int {
        val backgroundColors = mapOf<String, Int>(
            "aeimquy" to R.color.background_blue,
            "bfjnrvz" to R.color.background_red,
            "cgkosw" to R.color.background_green,
            "dhlptx" to R.color.background_purple
        )
        for (c in backgroundColors){
            if (name.lowercase() in c.key) { return c.value }
        }
        return R.color.white
    }

    fun colorForButtonAndHints(): Int {
        val buttonAndHintColors = mapOf<String, Int>(
            "aeimquy" to R.color.blue,
            "bfjnrvz" to R.color.red,
            "cgkosw" to R.color.green,
            "dhlptx" to R.color.purple
        )
        for (c in buttonAndHintColors){
            if (name.lowercase() in c.key) { return c.value }
        }
        return R.color.gray_light
    }

    fun colorForTexts(): Int {
        val textColors = mapOf<String, Int>(
            "aeimquy" to R.color.blue_text,
            "bfjnrvz" to R.color.red_text,
            "cgkosw" to R.color.green_text,
            "dhlptx" to R.color.purple_text
        )
        for (c in textColors) {
            if (name.lowercase() in c.key) { return c.value }
        }
        return R.color.black
    }
}
fun Division.toDivisionForUi(): DivisionForUi{
    return DivisionForUi(
        name = this.name.char,
        description = this.description,
        exercises = this.exercises.map { it.toExerciseForUi() }.toMutableList(),
        status = true,
        message = null
    )
}