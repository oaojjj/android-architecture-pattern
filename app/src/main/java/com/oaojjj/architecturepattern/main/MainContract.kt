package com.oaojjj.architecturepattern.main

import com.oaojjj.architecturepattern.BasePresenter
import com.oaojjj.architecturepattern.BaseView

interface MainContract {
    interface View : BaseView<Presenter> {
        companion object {
            val TODOS_FRAGMENT_TAG = "AddEditTodoFragment"
            val ADD_EDIT_TODO_FRAGMENT_TAG = "TodosFragment"
        }

        var isChangeFragment: Boolean
        fun showTodosFragment()
        fun showAddEditTodoFragment()
        fun showBottomAppbar(isShow: Boolean)
        fun setExpandedAppBarLayout(isExpended: Boolean)
        fun showBottomAnimation(ResId: Int, fabAlignmentMode: Int)
    }

    interface Presenter : BasePresenter {
        fun setFragmentPresenter(vararg presenter: BasePresenter)
        fun <T> getFragmentPresenter(type: T): BasePresenter?
        fun addTodo()
    }
}