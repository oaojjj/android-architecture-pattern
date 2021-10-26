package com.oaojjj.architecturepattern.main

import androidx.fragment.app.Fragment

interface MainContract {
    interface View {
        fun showFragment(fragment: Fragment)
        fun showBottomAppbar(isShow: Boolean)
        fun setExpandedAppBarLayout(isExpended: Boolean)
    }

    interface Presenter {
        fun setView(view: View)
        fun addTodo()
    }
}