package com.example.threekingdomsreader.generals

import com.example.threekingdomsreader.BasePresenter
import com.example.threekingdomsreader.BaseView
import com.example.threekingdomsreader.data.General

interface GeneralsContract {
    interface View : BaseView<Presenter> {

        fun setLoadingIndicator(active: Boolean)

        fun showGenerals(generals: List<General>)

        fun showGeneral(general: General)

        fun showNoGenerals()

        fun showLoadingGeneralsError()

        fun showAllGeneralsFilterText()

        fun showMarkGeneralsFilterText()
    }

    interface Presenter : BasePresenter {

        var currentFiltering: GeneralFilterType

        fun loadGenerals(forceUpdate: Boolean)

        fun addGeneral()

        fun openGeneral(requestGeneral: General)
    }
}