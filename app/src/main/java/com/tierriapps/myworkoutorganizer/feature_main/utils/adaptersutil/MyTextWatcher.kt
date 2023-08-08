package com.tierriapps.myworkoutorganizer.feature_main.utils.adaptersutil


import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.View
import android.widget.EditText


class MyTextWatcher(val actualView: View,val nextView: View?, val function: () -> Unit): TextWatcher{
    init {
        actualView.setOnKeyListener { view, i, keyEvent ->
            if (i == KeyEvent.KEYCODE_ENTER && keyEvent.action == KeyEvent.ACTION_UP){
                nextView?.requestFocus()
                return@setOnKeyListener true
            }
            return@setOnKeyListener false
        }
    }
    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

    }

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        function.invoke()
    }

    override fun afterTextChanged(p0: Editable?) {

    }

}