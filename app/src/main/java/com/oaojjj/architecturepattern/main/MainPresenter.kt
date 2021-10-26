package com.oaojjj.architecturepattern.main

class MainPresenter : MainContract.Presenter {
    private lateinit var mainView: MainContract.View
    override fun setView(view: MainContract.View) {
        mainView = view
    }

    override fun addTodo() {
    }

}