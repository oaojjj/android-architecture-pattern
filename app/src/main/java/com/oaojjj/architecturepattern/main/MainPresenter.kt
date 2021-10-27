package com.oaojjj.architecturepattern.main

import com.oaojjj.architecturepattern.BasePresenter
import com.oaojjj.architecturepattern.todos.TodosContract

class MainPresenter(val mainView: MainContract.View?) : MainContract.Presenter {
    private lateinit var todosPresenter: TodosContract.Presenter

    override fun setTodosPresenter(presenter: BasePresenter) {
        todosPresenter = presenter as TodosContract.Presenter
    }

}