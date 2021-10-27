package com.oaojjj.architecturepattern.addtodo

import com.oaojjj.architecturepattern.BasePresenter
import com.oaojjj.architecturepattern.BaseView


interface AddTodoContract {
    interface View : BaseView<Presenter> {

    }

    interface Presenter : BasePresenter {
    }
}