package com.oaojjj.architecturepattern.listener

import android.view.View

interface OnTodoCheckBoxClickListener {
    fun onTodoCheckBoxClick(position: Int, checked: Boolean)
}