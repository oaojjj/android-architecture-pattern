package com.oaojjj.architecturepattern.todos

import com.oaojjj.architecturepattern.model.Todo

interface TodosContract {
    interface View {
        fun updateTodosView(item: MutableList<Todo>, position: Int)

    }

    interface Presenter {
        fun setView(view: View)
    }
}