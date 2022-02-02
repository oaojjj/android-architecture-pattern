package com.example.threekingdomsreader.general

import android.view.MenuItem
import com.example.threekingdomsreader.data.General
import com.example.threekingdomsreader.data.GeneralsRepository
import com.example.threekingdomsreader.data.source.GeneralsDataSource

class GeneralPresenter(
    val generalId: Long?,
    val generalRepository: GeneralsRepository,
    val view: GeneralContract.View
) : GeneralContract.Presenter {

    override var cachedGeneralImageUrl: String? = null

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
                    cachedGeneralImageUrl(general.image)
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
        newGeneral.image = cachedGeneralImageUrl ?: ""
        if (generalId == null) {
            createGeneral(newGeneral)
        } else {
            updateGeneral(newGeneral)
        }
    }

    private fun createGeneral(general: General) {
        if (general.isEmpty) {
            view.showEmptyGeneralError()
        } else {
            generalRepository.saveGeneral(general)
            view.showCreateGeneral()
            view.showGenerals()
        }
    }

    private fun updateGeneral(general: General) {
        general.id = generalId
        generalRepository.saveGeneral(general)
        view.showUpdateGeneral()
        view.showGenerals()
    }

    override fun cachedGeneralImageUrl(url: String) {
        cachedGeneralImageUrl = url
        view.showGeneralImage(url)
    }

    override fun deleteGeneral() {
        if (generalId == null) {
            view.showMissingGeneral()
        } else {
            generalRepository.deleteGeneral(generalId)
            view.showGeneralDeleted()
        }
    }

}
