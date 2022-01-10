package com.oaojjj.architecturepattern.todos

import androidx.fragment.app.Fragment
import com.oaojjj.architecturepattern.data.Todo
import com.oaojjj.architecturepattern.data.source.TodoRepository

class TodosPresenter(val todoRepository: TodoRepository, val view: TodosContract.View) :
    TodosContract.Presenter {

    fun getView(): Fragment = view as TodosFragment
    override fun removeTodo() {
        TODO("Not yet implemented")
    }

    override fun updateTodo() {
        TODO("Not yet implemented")
    }

    override fun openTodoDetails(clickedTodo: Todo) {
        view.showTodoDetailsUi(clickedTodo.id)
    }

    override fun completeTodo(completedTodo: Todo) {
        TODO("Not yet implemented")
    }

    override fun activateTodo(activatedTodo: Todo) {
        TODO("Not yet implemented")
    }

    override fun start() {
        TODO("Not yet implemented")
    }
}