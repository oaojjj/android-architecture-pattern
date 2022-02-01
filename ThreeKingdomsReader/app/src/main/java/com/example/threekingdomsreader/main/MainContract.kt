package com.example.threekingdomsreader.main

import com.example.threekingdomsreader.BasePresenter
import com.example.threekingdomsreader.BaseView
import com.example.threekingdomsreader.data.General
import com.example.threekingdomsreader.general.GeneralPresenter
import com.example.threekingdomsreader.generals.GeneralsPresenter

interface MainContract {
    interface View : BaseView<Presenter> {
        fun showGenerals()

        fun showGeneral(general: General?)

        fun fabHide()

        fun fabShow()
    }

    interface Presenter : BasePresenter {

        var generalPresenter: GeneralPresenter?

        var generalsPresenter: GeneralsPresenter?

        fun setFragmentPresenter(vararg presenters: BasePresenter)

        fun addEditDetailGeneral(general: General?)

        fun scrollControl(scrollY: Int, oldY: Int)
    }
}