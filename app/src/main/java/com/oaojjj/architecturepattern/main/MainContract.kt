package com.oaojjj.architecturepattern.main

import androidx.fragment.app.Fragment
import com.oaojjj.architecturepattern.BasePresenter
import com.oaojjj.architecturepattern.BaseView

interface MainContract {
    interface View : BaseView<Presenter> {
        fun showFragment(fragment: Fragment)
        fun showBottomAppbar(isShow: Boolean)
        fun setExpandedAppBarLayout(isExpended: Boolean)
        fun showBottomAnimation(ResId: Int, fabAlignmentMode: Int)
    }

    interface Presenter : BasePresenter {
        fun setTodosPresenter(presenter: BasePresenter)
        fun addTodo()
    }
}