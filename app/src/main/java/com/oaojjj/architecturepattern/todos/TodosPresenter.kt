package com.oaojjj.architecturepattern.todos

import androidx.fragment.app.Fragment

class TodosPresenter(private val view: TodosContract.View?) : TodosContract.Presenter {

    fun getView(): Fragment = view as TodosFragment
}