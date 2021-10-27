package com.oaojjj.architecturepattern.main

import com.oaojjj.architecturepattern.BasePresenter
import com.oaojjj.architecturepattern.BaseView

interface MainContract {
    interface View : BaseView<Presenter> {
        var isChangeFragment: Boolean
        fun showTodosFragment()
        fun showAddTodoFragment()
        fun showBottomAppbar(isShow: Boolean)
        fun setExpandedAppBarLayout(isExpended: Boolean)
        fun showBottomAnimation(ResId: Int, fabAlignmentMode: Int)
    }

    interface Presenter : BasePresenter {
        fun setTodosPresenter(presenter: BasePresenter)
    }
}