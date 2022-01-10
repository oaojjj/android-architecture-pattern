package com.oaojjj.architecturepattern.addedittodo

import androidx.fragment.app.Fragment


class AddEditTodoPresenter(private val view: AddEditTodoContract.View?) :
    AddEditTodoContract.Presenter {
    fun getView(): Fragment = view as AddEditTodoFragment
    override fun saveTodo() {
        TODO("Not yet implemented")
    }

    override fun start() {
        TODO("Not yet implemented")
    }
}