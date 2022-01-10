package com.oaojjj.architecturepattern.main

import androidx.fragment.app.Fragment
import com.oaojjj.architecturepattern.BasePresenter
import com.oaojjj.architecturepattern.BaseView
import com.oaojjj.architecturepattern.addedittodo.AddEditTodoContract
import com.oaojjj.architecturepattern.addedittodo.AddEditTodoFragment
import com.oaojjj.architecturepattern.addedittodo.AddEditTodoPresenter
import com.oaojjj.architecturepattern.todos.TodosContract
import com.oaojjj.architecturepattern.todos.TodosFragment
import com.oaojjj.architecturepattern.todos.TodosPresenter

interface MainContract {

    interface View : BaseView<Presenter> {

        companion object {

            const val TODOS_FRAGMENT_TAG = "AddEditTodoFragment"

            const val ADD_EDIT_TODO_FRAGMENT_TAG = "TodosFragment"
        }

        fun navigateTodosFragment()

        fun navigateAddEditFragment()

        fun instantiateTodosFragment()

        fun instantiateAddEditFragment()

        fun showTodosFragment()

        fun showAddEditTodoFragment()

        fun showBottomAppbar(isShow: Boolean)

        fun setExpandedAppBarLayout(isExpended: Boolean)

        fun changeFabIconToPlus()

        fun changeFabIconToCheck()

        fun showBottomAnimation(ResId: Int, fabAlignmentMode: Int)
        fun getFragmentByName(name: String): Fragment
        fun getCurrentFragment(): Fragment?
    }

    interface Presenter : BasePresenter {
        var todosPresenter: TodosPresenter
        var addEditTodoPresenter: AddEditTodoPresenter

        fun setFragmentPresenter(vararg presenters: BasePresenter)
    }
}