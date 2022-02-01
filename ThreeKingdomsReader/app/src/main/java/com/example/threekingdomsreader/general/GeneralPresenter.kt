package com.example.threekingdomsreader.general

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
            populateTask()
        }
    }

    override fun populateTask() {
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

}