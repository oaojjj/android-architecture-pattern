package com.example.threekingdomsreader.general

import android.util.Log
import android.view.MenuItem
import com.example.threekingdomsreader.data.General
import com.example.threekingdomsreader.data.GeneralsRepository
import com.example.threekingdomsreader.data.source.GeneralsDataSource

class GeneralPresenter(
    val generalId: Long?,
    val generalRepository: GeneralsRepository,
    val view: GeneralContract.View
) : GeneralContract.Presenter {

    init {
        view.presenter = this
    }

    override fun start() {
        if (generalId != null) {
            populateGeneral()
        } else {
            view.showEmptyGeneral()
        }
    }

    override fun populateGeneral() {
        generalRepository.getGeneral(generalId, object : GeneralsDataSource.GetGeneralCallback {
            override fun onGeneralLoaded(general: General) {
                if (view.isActive) {
                    view.setGeneral(general)
                }
            }

            override fun onDataNotAvailable() {
                if (view.isActive) {
                    view.showEmptyGeneralError()
                }
            }

        })
    }

    override fun checkViewState(item: MenuItem, lock: Boolean) {
        when (lock) {
            true -> view.unableEditView(item)
            false -> view.enableEditView(item)
        }
    }

    override fun saveGeneral(newGeneral: General) {
        Log.d("generalPresenter", "saveGeneral: $newGeneral")
    }

}