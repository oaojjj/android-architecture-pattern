package com.example.threekingdomsreader.data

import android.util.Log
import com.example.threekingdomsreader.data.source.GeneralsDataSource
import java.util.LinkedHashMap

class GeneralsRepository(
    val generalsLocalDataSource: GeneralsDataSource
) : GeneralsDataSource {

    var cachedGenerals: LinkedHashMap<Long, General> = LinkedHashMap()

    /**
     * 다음에 데이터를 요청할 때, 강제로 업데이트하려면 캐시를 유효하지 않은 것(invalid)으로 표시한다. -> false == invalid?
     */
    var cacheIsDirty = false

    override fun getGenerals(callback: GeneralsDataSource.LoadGeneralsCallback) {
        // 캐시가 되어있고, 갱신이 필요없는 경우 캐시로 즉시 응답
        if (cachedGenerals.isNotEmpty() && !cacheIsDirty) {
            callback.onGeneralsLoaded(ArrayList(cachedGenerals.values))
            return
        }

        Log.d("database", "getGenerals: $cacheIsDirty")
        if (cacheIsDirty) {
            // 갱신이 필요하면 네트워크에서 새 데이터를 가져온다.
            getGeneralsFromRemoteDataSource(callback)
        } else {
            // 로컬 저장소의 사용이 가능한 경우 요청, 그렇지 않으면 네트워크 요청
            generalsLocalDataSource.getGenerals(object : GeneralsDataSource.LoadGeneralsCallback {
                override fun onGeneralsLoaded(generals: List<General>) {
                    refreshCache(generals)
                    callback.onGeneralsLoaded(ArrayList(cachedGenerals.values))
                }

                override fun onDataNotAvailable() {
                    getGeneralsFromRemoteDataSource(callback)
                }
            })
        }
    }


    override fun getGeneral(generalId: Long?, callback: GeneralsDataSource.GetGeneralCallback) {
        val generalInCache = cachedGenerals[generalId]

        if (generalInCache != null) {
            callback.onGeneralLoaded(generalInCache)
            return
        }

        generalsLocalDataSource.getGeneral(generalId,
            object : GeneralsDataSource.GetGeneralCallback {
                override fun onGeneralLoaded(general: General) {
                    cacheAndPerform(general) {
                        callback.onGeneralLoaded(it)
                    }
                }

                override fun onDataNotAvailable() {
                    callback.onDataNotAvailable()
                }
            })
    }

    private fun refreshCache(generals: List<General>) {
        cachedGenerals.clear()
        generals.forEach { cacheAndPerform(it) {} }
        cacheIsDirty = false
    }

    private fun cacheAndPerform(general: General, perform: (General) -> Unit) =
        with(general) {
            if (id == null) id = cachedGenerals[cachedGenerals.size.toLong()]?.id?.plus(1)
            val cachedGeneral =
                General(name, sex, image, belong, position, birth, death, description, id)
            cachedGenerals[cachedGeneral.id!!] = cachedGeneral
            perform(cachedGeneral)
        }


    // 네트워크에 데이터 요청
    private fun getGeneralsFromRemoteDataSource(callback: GeneralsDataSource.LoadGeneralsCallback) {
        // only local이기 때문에 데이터가 없다고 반환
        callback.onGeneralsLoaded(ArrayList(0))
    }


    override fun saveGeneral(general: General) {
        cacheAndPerform(general) {
            generalsLocalDataSource.saveGeneral(general)
        }
    }

    override fun refreshGenerals() {
        cacheIsDirty = true
    }

    override fun deleteAllGenerals() {
        TODO("Not yet implemented")
    }

    override fun deleteGeneral(generalId: Long) {
        generalsLocalDataSource.deleteGeneral(generalId)
        cachedGenerals.remove(generalId)
    }

    companion object {
        private var INSTANCE: GeneralsRepository? = null

        @JvmStatic
        fun getInstance(generalsLocalDataSource: GeneralsDataSource): GeneralsRepository {
            return INSTANCE ?: GeneralsRepository(generalsLocalDataSource).apply { INSTANCE = this }
        }


    }
}