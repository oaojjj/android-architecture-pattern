package com.example.threekingdomsreader.general

import android.view.MenuItem
import com.example.threekingdomsreader.BasePresenter
import com.example.threekingdomsreader.BaseView
import com.example.threekingdomsreader.data.General

/**
 * GeneralFragment는 무장 추가, 변경, 상세 내용등의 기능을
 *  통합하여 전부 한 화면에서 처리하게끔 구성
 */
interface GeneralContract {

    interface View : BaseView<Presenter> {
        fun setGeneral(general: General)

        fun showEmptyGeneralError()

        fun showEmptyGeneral()

        fun enableEditView(item: MenuItem)

        fun unableEditView(item: MenuItem)
    }

    interface Presenter : BasePresenter {
        fun populateGeneral()

        fun checkViewState(item: MenuItem, lock: Boolean)

        fun saveGeneral(newGeneral: General)
    }

}