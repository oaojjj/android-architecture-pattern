package com.oaojjj.architecturepattern.listener

import android.view.View

interface OnTodoClickListener {
    fun onTodoCheckClick(position: Int, checked: Boolean)
    fun onTodoLongClick(view: View?, position: Int)
}