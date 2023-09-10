package com.tierriapps.myworkoutorganizer.feature_main.presenter.customviews

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.util.AttributeSet
import android.view.View
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
        this.textSize = 30f
        this.setTextColor(Color.WHITE)
        val params = MarginLayoutParams(MarginLayoutParams.WRAP_CONTENT,MarginLayoutParams.MATCH_PARENT)
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
            // Crie um GradientDrawable para definir a forma e a cor de fundo
            val backgroundDrawable = GradientDrawable()
            backgroundDrawable.shape = GradientDrawable.OVAL // Forma retangular
            backgroundDrawable.setColor(bkc)
            this.background = backgroundDrawable
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        // Obtém a altura da view
        val height = measuredHeight

        // Define a largura igual à altura para tornar o botão redondo
        setMeasuredDimension(height, height)
    }


    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)

        // Obtém a altura da view
        val height = measuredHeight

        // Define a largura igual à altura para tornar o botão redondo
        setMeasuredDimension(height, height)

        // Centraliza o texto horizontalmente
        val centerX = (left + right) / 2
        val textWidth = paint.measureText(text.toString())
        val textX = centerX - textWidth / 2

        // Centraliza o texto verticalmente
        val centerY = (top + bottom) / 2
        val textHeight = paint.descent() - paint.ascent()
        val textY = centerY - textHeight / 2

        setPadding(
            (textX - left).toInt(),
            (textY - top).toInt(),
            (right - centerX - textWidth / 2).toInt(),
            (bottom - centerY - textHeight / 2).toInt()
        )
    }

}