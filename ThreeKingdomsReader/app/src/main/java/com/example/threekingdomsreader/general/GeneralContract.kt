package com.example.threekingdomsreader.general

import com.example.threekingdomsreader.BasePresenter
import com.example.threekingdomsreader.BaseView

// add, edit, detail
interface GeneralContract {

    interface View : BaseView<Presenter> {

    }

    interface Presenter : BasePresenter {}

}