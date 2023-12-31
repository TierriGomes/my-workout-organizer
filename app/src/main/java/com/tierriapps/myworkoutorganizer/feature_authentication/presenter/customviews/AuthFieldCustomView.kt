package com.tierriapps.myworkoutorganizer.customviews

import android.content.Context
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.AttributeSet
import android.view.Gravity
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.tierriapps.myworkoutorganizer.R
import com.tierriapps.myworkoutorganizer.databinding.CustomviewAuthfieldBinding


class AuthFieldCustomView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet?, defStyleAttrs: Int = 0):
    ConstraintLayout(context, attrs, defStyleAttrs) {

    private val binding = CustomviewAuthfieldBinding
        .inflate(LayoutInflater.from(context), this, true)

    private val textField: EditText = binding.etText
    private var textTopHint: TextView = binding.tvTopHint
    private var eyeButton: ImageView = binding.imgbtnVisibility
    private var imgRight: ImageView = binding.imgRight
    private var imgWrong: ImageView = binding.imgWrong

    private var funTextValidator: ((text: String) -> Boolean)? = null
    private var isTextVisible = true
    init {
        if (attrs != null) {
            val attributeSet = context.obtainStyledAttributes(attrs, R.styleable.AuthFieldCustomView)

            textField.textSize =
                attributeSet.getDimension(R.styleable.AuthFieldCustomView_textSize, 16f) / 2
            textField.hint =
                attributeSet.getText(R.styleable.AuthFieldCustomView_textHint)
            textField.gravity =
                when(attributeSet.getInt(R.styleable.AuthFieldCustomView_textGravity, 1)){
                1 -> Gravity.LEFT
                2 -> Gravity.CENTER
                else -> Gravity.RIGHT }

            textTopHint.textSize =
                attributeSet.getDimension(R.styleable.AuthFieldCustomView_topHintSize, 14f) / 2
            textTopHint.text =
                attributeSet.getText(R.styleable.AuthFieldCustomView_topHintText)
            textTopHint.gravity =
                when(attributeSet.getInt(R.styleable.AuthFieldCustomView_topHintGravity, 1)){
                    1 -> Gravity.LEFT
                    2 -> Gravity.CENTER
                    else -> Gravity.RIGHT
                }

            val eyeBtVisibility = attributeSet.getBoolean(R.styleable.AuthFieldCustomView_visibilityButton, false)
            if (eyeBtVisibility) {
                eyeButton.visibility = View.VISIBLE
                textField.inputType = InputType.TYPE_TEXT_VARIATION_PASSWORD
            }
            eyeButton.setOnClickListener { onEyeButtonClicked() }
            isTextVisible = attributeSet.getBoolean(R.styleable.AuthFieldCustomView_textVisibility, true)
            if (!isTextVisible){
                textField.transformationMethod = PasswordTransformationMethod.getInstance()
            }
            attributeSet.recycle()
        }
    }
    fun getText() = textField.text.toString()
    fun setText(text: String){ textField.setText(text)}

    fun setOnKeyListener(lambda: (view: View, keyCode: Int, keyEvent: KeyEvent) -> Boolean){
        textField.setOnKeyListener { view, keyCode, keyEvent ->
            lambda(view, keyCode, keyEvent)
        }
    }

    fun setTextValidatorRule(rule: (text: String) -> Boolean){
        funTextValidator = rule
        val textWatcher = MyInsideTextWatcher()
        textField.addTextChangedListener(textWatcher)
    }

    private fun onEyeButtonClicked(){
        val passInvisible = PasswordTransformationMethod.getInstance()
        val passVisible = HideReturnsTransformationMethod.getInstance()
        if (isTextVisible){
            isTextVisible = false
            textField.transformationMethod = passInvisible
            eyeButton.setImageResource(R.drawable.vector_eye_invisible)
        }else{
            isTextVisible = true
            textField.transformationMethod = passVisible
            eyeButton.setImageResource(R.drawable.vector_eye_visible)
        }
        textField.setSelection(textField.text.length)
    }

    inner class MyInsideTextWatcher(): TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            return
        }
        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            if (funTextValidator?.invoke(getText()) == true){
                imgRight.visibility = View.VISIBLE
                imgWrong.visibility = View.GONE
            }else{
                imgRight.visibility = View.GONE
                imgWrong.visibility = View.VISIBLE
            }
        }
        override fun afterTextChanged(p0: Editable?) {
            return
        }

    }


}