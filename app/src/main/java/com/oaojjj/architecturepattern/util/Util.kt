package com.oaojjj.architecturepattern.util

import android.content.Context
import android.view.View
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
    fun hideInput(view: View?) {
        mInputMethodManager?.hideSoftInputFromWindow(view?.windowToken, 0)
        view?.clearFocus()
        mInputMethodManager = null
    }
}