package com.oaojjj.architecturepattern.todos

import com.oaojjj.architecturepattern.BasePresenter
import com.oaojjj.architecturepattern.BaseView
import com.oaojjj.architecturepattern.data.Todo
import com.oaojjj.architecturepattern.data.source.TodoRepository

interface TodosContract {
    interface View : BaseView<Presenter> {
        fun showTodoDetailsUi(id: Long?)
    }

    interface Presenter : BasePresenter {
        fun removeTodo()
        fun updateTodo()
        fun openTodoDetails(clickedTodo: Todo)
        fun completeTodo(completedTodo: Todo)
        fun activateTodo(activatedTodo: Todo)
    }
}