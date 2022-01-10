package com.oaojjj.architecturepattern.addedittodo

import com.oaojjj.architecturepattern.BasePresenter
import com.oaojjj.architecturepattern.BaseView


interface AddEditTodoContract {
    interface View : BaseView<Presenter> {
        fun showInput()
    }

    interface Presenter : BasePresenter {
        fun saveTodo()
    }
}