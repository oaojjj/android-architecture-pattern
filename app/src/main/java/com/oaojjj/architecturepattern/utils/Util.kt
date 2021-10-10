package com.oaojjj.architecturepattern.utils

import android.content.Context
import android.view.inputmethod.InputMethodManager
import android.widget.EditText

object Util {
    private var mInputMethodManager: InputMethodManager? = null

    // 키보드 올리기
    fun showInput(context: Context, editText: EditText) {
        editText.requestFocus()
        mInputMethodManager =
            context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        mInputMethodManager?.showSoftInput(
            editText,
            InputMethodManager.SHOW_FORCED
        )
    }

    // 키보드 내리기
    fun hideInput(editText: EditText) {
        mInputMethodManager?.hideSoftInputFromWindow(editText.windowToken, 0)
        mInputMethodManager = null
    }
}