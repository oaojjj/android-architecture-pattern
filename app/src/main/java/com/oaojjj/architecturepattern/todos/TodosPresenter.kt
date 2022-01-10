package com.oaojjj.architecturepattern.todos

import androidx.fragment.app.Fragment

class TodosPresenter(private val view: TodosContract.View?) : TodosContract.Presenter {

    fun getView(): Fragment = view as TodosFragment
    override fun removeTodo() {
        TODO("Not yet implemented")
    }

    override fun updateTodo() {
        TODO("Not yet implemented")
    }

    override fun start() {
        TODO("Not yet implemented")
    }
}