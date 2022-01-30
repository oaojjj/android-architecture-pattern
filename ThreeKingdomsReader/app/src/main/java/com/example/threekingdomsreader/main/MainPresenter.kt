package com.example.threekingdomsreader.main

import com.example.threekingdomsreader.BasePresenter
import com.example.threekingdomsreader.general.GeneralContract
import com.example.threekingdomsreader.general.GeneralPresenter
import com.example.threekingdomsreader.generals.GeneralsContract
import com.example.threekingdomsreader.generals.GeneralsPresenter

class MainPresenter(val view: MainContract.View) : MainContract.Presenter {

    init {
        view.presenter = this
    }

    override var generalPresenter: GeneralPresenter
        get() = TODO("Not yet implemented")
        set(value) {}

    override var generalsPresenter: GeneralsPresenter
        get() = TODO("Not yet implemented")
        set(value) {}

    override fun setFragmentPresenter(vararg presenters: BasePresenter) {
        for (presenter in presenters) {
            when (presenter) {
                is GeneralContract.Presenter -> generalPresenter =
                    presenter as GeneralPresenter
                is GeneralsContract.Presenter -> generalsPresenter =
                    presenter as GeneralsPresenter
            }
        }
    }

    override fun start() {
        TODO("Not yet implemented")
    }
}