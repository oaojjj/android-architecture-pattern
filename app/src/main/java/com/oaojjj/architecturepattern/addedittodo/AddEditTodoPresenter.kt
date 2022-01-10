package com.oaojjj.architecturepattern.addedittodo

import androidx.fragment.app.Fragment
import com.oaojjj.architecturepattern.data.source.TodoRepository
import com.oaojjj.architecturepattern.todos.TodosFragment


class AddEditTodoPresenter(
    val todoRepository: TodoRepository,
    val view: AddEditTodoContract.View?
) : AddEditTodoContract.Presenter {
    fun getView(): AddEditTodoFragment = view as AddEditTodoFragment

    init {
        view?.presenter = this
    }

    override fun saveTodo() {
        TODO("Not yet implemented")
    }

    override fun start() {
        TODO("Not yet implemented")
    }
}