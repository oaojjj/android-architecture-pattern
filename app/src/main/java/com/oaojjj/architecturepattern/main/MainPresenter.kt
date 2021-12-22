package com.oaojjj.architecturepattern.main

import com.google.android.material.bottomappbar.BottomAppBar
import com.oaojjj.architecturepattern.BasePresenter
import com.oaojjj.architecturepattern.R
import com.oaojjj.architecturepattern.addedittodo.AddEditTodoContract
import com.oaojjj.architecturepattern.addedittodo.AddEditTodoPresenter
import com.oaojjj.architecturepattern.todos.TodosContract
import com.oaojjj.architecturepattern.todos.TodosPresenter

class MainPresenter(val view: MainContract.View) : MainContract.Presenter {
    override lateinit var todosPresenter: TodosPresenter
    override lateinit var addEditTodoPresenter: AddEditTodoPresenter

    /**
     * @param presenters fragment presenters hosted in single activity
     */
    override fun setFragmentPresenter(vararg presenters: BasePresenter) {
        for (presenter in presenters) {
            when (presenter) {
                is TodosContract.Presenter -> todosPresenter =
                    presenter as TodosPresenter
                is AddEditTodoContract.Presenter -> addEditTodoPresenter =
                    presenter as AddEditTodoPresenter
            }
        }
    }

    // MainActivity에서 사용자 이벤트(Todo추가)를 받아서 처리
    override fun addTodo() {
        with(view) {
            this.navigateAddEditTodoFragment()
            this.showBottomAnimation(R.drawable.check, BottomAppBar.FAB_ALIGNMENT_MODE_END)
        }
    }
}