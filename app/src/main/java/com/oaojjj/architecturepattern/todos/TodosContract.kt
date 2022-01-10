package com.oaojjj.architecturepattern.todos

import com.oaojjj.architecturepattern.BasePresenter
import com.oaojjj.architecturepattern.BaseView
import com.oaojjj.architecturepattern.data.Todo

interface TodosContract {
    interface View : BaseView<Presenter> {
        fun updateTodosView(item: MutableList<Todo>, position: Int)
    }

    interface Presenter : BasePresenter {
        fun removeTodo()
        fun updateTodo()
    }
}