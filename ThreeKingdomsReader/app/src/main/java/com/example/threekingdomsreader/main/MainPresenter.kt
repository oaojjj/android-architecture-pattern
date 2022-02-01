package com.example.threekingdomsreader.main

import com.example.threekingdomsreader.BasePresenter
import com.example.threekingdomsreader.data.General
import com.example.threekingdomsreader.general.GeneralContract
import com.example.threekingdomsreader.general.GeneralPresenter
import com.example.threekingdomsreader.generals.GeneralsContract
import com.example.threekingdomsreader.generals.GeneralsPresenter

class MainPresenter(val view: MainContract.View) : MainContract.Presenter {

    init {
        view.presenter = this
    }

    override fun start() {
    }

    override var generalPresenter: GeneralPresenter? = null

    override var generalsPresenter: GeneralsPresenter? = null

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

    override fun addEditDetailGeneral(general: General?) {
        view.showGeneral(general)
    }

    override fun scrollControl(scrollY: Int, oldY: Int) {
        if (scrollY > oldY) {
            view.fabHide()
        } else if (scrollY < oldY) {
            view.fabShow()
        }
    }

}