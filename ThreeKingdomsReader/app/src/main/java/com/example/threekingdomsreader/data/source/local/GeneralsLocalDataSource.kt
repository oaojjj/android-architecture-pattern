package com.example.threekingdomsreader.data.source.local

import com.example.threekingdomsreader.data.General
import com.example.threekingdomsreader.data.source.GeneralsDataSource
import com.example.threekingdomsreader.util.AppExecutors

class GeneralsLocalDataSource private constructor(
    val appExecutors: AppExecutors,
    val generalDao: GeneralDao
) : GeneralsDataSource {

    /**
     * Note: [GeneralsDataSource.LoadGeneralsCallback.onDataNotAvailable]
     * 데이터베이스가 존재하지 않거나, 테이블이 비어 있으면 호출된다.
     */
    override fun getGenerals(callback: GeneralsDataSource.LoadGeneralsCallback) {
        appExecutors.diskIO.execute {
            val generals = generalDao.getGenerals()
            appExecutors.mainThread.execute {
                if (generals.isEmpty()) {
                    callback.onDataNotAvailable()
                } else {
                    callback.onGeneralsLoaded(generals)
                }
            }
        }
    }

    /**
     * Note: [GeneralsDataSource.LoadGeneralsCallback.onDataNotAvailable]
     * 무장을 찾지 못한 경우 호출된다.
     */
    override fun getGeneral(generalId: Long?, callback: GeneralsDataSource.GetGeneralCallback) {
        appExecutors.diskIO.execute {
            val general = generalDao.getGeneralById(generalId)
            appExecutors.mainThread.execute {
                if (general != null) {
                    callback.onGeneralLoaded(general)
                } else {
                    callback.onDataNotAvailable()
                }
            }
        }
    }

    override fun saveGeneral(general: General) {
        appExecutors.diskIO.execute { generalDao.insertGeneral(general) }
    }

    override fun refreshGenerals() {
        TODO("Not yet implemented")
    }

    override fun deleteAllGenerals() {
        TODO("Not yet implemented")
    }

    override fun deleteGeneral(generalId: Long) {
        TODO("Not yet implemented")
    }

    companion object {
        private var INSTANCE: GeneralsLocalDataSource? = null

        @JvmStatic
        fun getInstance(
            appExecutors: AppExecutors,
            generalDao: GeneralDao
        ): GeneralsLocalDataSource {
            if (INSTANCE == null) {
                synchronized(GeneralsLocalDataSource::javaClass) {
                    INSTANCE = GeneralsLocalDataSource(appExecutors, generalDao)
                }

            }
            return INSTANCE!!
        }
    }
}