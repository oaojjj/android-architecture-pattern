package com.example.threekingdomsreader.data.source

import com.example.threekingdomsreader.data.General

interface GeneralsDataSource {

    interface LoadGeneralsCallback {

        fun onGeneralsLoaded(generals: List<General>)

        fun onDataNotAvailable()
    }

    interface GetGeneralCallback {

        fun onGeneralLoaded(general: General)

        fun onDataNotAvailable()
    }

    fun getGenerals(callback: LoadGeneralsCallback)

    fun getGeneral(generalId: Long?, callback: GetGeneralCallback)

    fun saveGeneral(general: General)

    fun refreshGenerals()

    fun deleteAllGenerals()

    fun deleteGeneral(generalId: Long)
}