package com.oaojjj.architecturepattern.listener

interface OnTodoClickListener {
    fun onTodoCheckClickListener(position: Int, checked: Boolean)
    fun onTodoLongClickListener()
}