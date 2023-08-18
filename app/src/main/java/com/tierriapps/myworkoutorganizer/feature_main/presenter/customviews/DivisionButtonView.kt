package com.tierriapps.myworkoutorganizer.feature_main.presenter.customviews

import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Build
import android.util.AttributeSet
import android.view.ViewGroup.LayoutParams
import android.view.ViewGroup.MarginLayoutParams
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.tierriapps.myworkoutorganizer.R


class DivisionButtonView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
): AppCompatButton(context, attrs, defStyleAttr) {
    private var divisionChar = 'a'
    private var buttonColor = 0


    init {
        this.setBackgroundResource(R.drawable.selector_division_button)
        this.typeface = ResourcesCompat.getFont(context, R.font.caesar_dressing)
        this.textAlignment = TEXT_ALIGNMENT_CENTER
        this.textSize = 30f
        this.setTextColor(Color.WHITE)

        val params = MarginLayoutParams(100, LayoutParams.MATCH_PARENT)

        params.marginEnd = 3
        params.marginStart = 3
        this.layoutParams = params
        setPadding(0, 10, 0, 0)

        val char = if (text.isNotEmpty()) text[0] else divisionChar
        defAttrsWithNewChar(char)
    }

    override fun setText(text: CharSequence?, type: BufferType?) {
        if(text?.toString() != ""){
            divisionChar = text.toString()[0].lowercaseChar()
            defAttrsWithNewChar(divisionChar)
        }
        super.setText(text, type)
    }

    private fun defAttrsWithNewChar(char: Char){
        val buttonColors = mapOf<String, Int>(
            "aeimquy" to R.color.blue,
            "bfjnrvz" to R.color.red,
            "cgkosw" to R.color.green,
            "dhlptx" to R.color.purple
        )
        for (k in buttonColors){
            if (char in k.key){
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