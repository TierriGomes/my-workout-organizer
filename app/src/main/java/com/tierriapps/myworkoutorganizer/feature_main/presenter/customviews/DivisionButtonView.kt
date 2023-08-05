package com.tierriapps.myworkoutorganizer.feature_main.presenter.customviews

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.PorterDuff
import android.os.Build
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import com.tierriapps.myworkoutorganizer.R

class DivisionButtonView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet, defStyleAttr: Int = 0
): AppCompatButton(context, attrs, defStyleAttr) {
    private var divisionChar = 'a'
    private var backGroundColor = 0
    private var buttonColor = 0

    init {
        val char = if (text.isNotEmpty()) text[0] else divisionChar
        defAttrsWithNewChar(char)
    }
    fun getBackGroundColor() = backGroundColor
    fun getButtonColor() = buttonColor

    override fun setText(text: CharSequence?, type: BufferType?) {
        if(text?.toString() != ""){
            divisionChar = text.toString()[0].lowercaseChar()
            defAttrsWithNewChar(divisionChar)
        }
        super.setText(text, type)
    }

    private fun defAttrsWithNewChar(char: Char){
        val backgroundColors = mapOf<String, Int>(
            "aeimquy" to R.color.background_blue,
            "bfjnrvz" to R.color.background_red,
            "cgkosw" to R.color.background_green,
            "dhlptx" to R.color.background_purple
        )
        val buttonColors = mapOf<String, Int>(
            "aeimquy" to R.color.blue,
            "bfjnrvz" to R.color.red,
            "cgkosw" to R.color.green,
            "dhlptx" to R.color.purple
        )
        for (k in backgroundColors){
            if (char in k.key){
                backGroundColor = k.value
                buttonColor = buttonColors[k.key]!!
                break
            }
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            this.backgroundTintMode = PorterDuff.Mode.MULTIPLY
            this.backgroundTintList = ContextCompat.getColorStateList(context, buttonColor)

        }else {
            val bkc = ContextCompat.getColor(context, buttonColor)
            this.background.setColorFilter(bkc, PorterDuff.Mode.SRC_ATOP)
        }
    }




}