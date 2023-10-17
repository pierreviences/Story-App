package com.example.storyapp.ui.custom

import android.content.Context
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.Patterns
import com.example.storyapp.R
import com.google.android.material.textfield.TextInputEditText

class MyEmailEditText : TextInputEditText {

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init()
    }

    private fun init() {
        val errorMessage = resources.getString(R.string.email_invalid)

        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s != null) {
                    error = if (s.isEmail()) null else errorMessage
                }
            }

            override fun afterTextChanged(s: android.text.Editable?) {}
        })
    }

    private fun CharSequence.isEmail(): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(this).matches()
    }
}