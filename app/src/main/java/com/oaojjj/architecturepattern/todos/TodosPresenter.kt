package com.oaojjj.architecturepattern.todos

class TodosPresenter : TodosContract.Presenter {
    private var todosView: TodosContract.View? = null

    override fun setView(view: TodosContract.View) {
        todosView = view
    }
}