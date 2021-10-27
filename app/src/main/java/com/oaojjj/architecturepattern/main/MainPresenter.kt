package com.oaojjj.architecturepattern.main

import com.google.android.material.bottomappbar.BottomAppBar
import com.oaojjj.architecturepattern.BasePresenter
import com.oaojjj.architecturepattern.R
import com.oaojjj.architecturepattern.addedittodo.AddEditTodoContract
import com.oaojjj.architecturepattern.todos.TodosContract

class MainPresenter(private val view: MainContract.View?) : MainContract.Presenter {
    private var todosPresenter: TodosContract.Presenter? = null
    private var addEditTodoPresenter: AddEditTodoContract.Presenter? = null

    /**
     * @param presenter fragment presenters hosted in single activity
     */
    override fun setFragmentPresenter(vararg presenter: BasePresenter) {
        when (presenter) {
            is TodosContract.Presenter -> todosPresenter = presenter
            is AddEditTodoContract.Presenter -> addEditTodoPresenter = presenter
        }
    }

    /**
     * @param T fragment views hosted in single activity
     */
    override fun <T> getFragmentPresenter(type: T): BasePresenter? {
        return when (type) {
            is TodosContract.View -> todosPresenter
            is AddEditTodoContract.View -> addEditTodoPresenter
            else -> null
        }
    }

    // MainActivity에서 사용자 이벤트(Todo추가)를 받아서 처리
    override fun addTodo() {
        with(view) {
            this?.showAddEditTodoFragment()
            this?.showBottomAnimation(R.drawable.check, BottomAppBar.FAB_ALIGNMENT_MODE_END)
            this?.isChangeFragment = true
        }
    }
}