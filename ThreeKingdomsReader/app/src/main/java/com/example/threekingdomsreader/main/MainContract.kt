package com.example.threekingdomsreader.main

import com.example.threekingdomsreader.BasePresenter
import com.example.threekingdomsreader.BaseView
import com.example.threekingdomsreader.general.GeneralPresenter
import com.example.threekingdomsreader.generals.GeneralsPresenter

interface MainContract {
    interface View : BaseView<Presenter> {

    }

    interface Presenter : BasePresenter {
        var generalPresenter: GeneralPresenter
        var generalsPresenter: GeneralsPresenter

        fun setFragmentPresenter(vararg presenters: BasePresenter)
    }
}