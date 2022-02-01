package com.example.threekingdomsreader.generals

import android.util.Log
import com.example.threekingdomsreader.data.General
import com.example.threekingdomsreader.data.GeneralsRepository
import com.example.threekingdomsreader.data.source.GeneralsDataSource

class GeneralsPresenter(
    val generalRepository: GeneralsRepository,
    val view: GeneralsContract.View
) : GeneralsContract.Presenter {

    override var currentFiltering: GeneralFilterType = GeneralFilterType.ALL_GENERALS

    private var firstLoad = true

    init {
        view.presenter = this
    }

    override fun start() {
        loadGenerals(false)
    }

    override fun loadGenerals(forceUpdate: Boolean) {
        // 네트워크에서 값을 가져오는 것은 처음 호출시 강제로 실행한다.
        loadGenerals(forceUpdate || firstLoad, true)
        firstLoad = false
    }

    /**
     * @param forceUpdate   데이터를 갱신하려면 true 전달한다. [GeneralsDataSource]
     * *
     * @param showLoadingUI UI 로딩 아이콘을 표시하려면 true 전달한다.
     */
    private fun loadGenerals(forceUpdate: Boolean, showLoadingUI: Boolean) {
        if (showLoadingUI) {
            view.setLoadingIndicator(true)
        }
        /*if (forceUpdate) {
            generalRepository.refreshGenerals()
        }*/

        generalRepository.getGenerals(object : GeneralsDataSource.LoadGeneralsCallback {
            override fun onGeneralsLoaded(generals: List<General>) {
                val generalsToShow = ArrayList<General>()

                for (general in generals) {
                    when (currentFiltering) {
                        GeneralFilterType.ALL_GENERALS -> generalsToShow.add(general)
                        GeneralFilterType.MARK_GENERALS -> {
                            // 즐겨찾기
                        }
                    }
                }

                Log.d("database", "onGeneralsLoaded: ${view.isActive}")
                // View가 더 이상 UI 업데이트를 처리하지 못할 수 있다.
                if (!view.isActive) return

                if (showLoadingUI) {
                    view.setLoadingIndicator(false)
                }

                processGenerals(generalsToShow)
            }

            override fun onDataNotAvailable() {
                // View가 더 이상 UI 업데이트를 처리하지 못할 수 있다.
                if (!view.isActive) return

                view.showLoadingGeneralsError()
            }
        })
    }

    private fun processGenerals(generals: List<General>) {
        if (generals.isNotEmpty()) {
            // 목록 표시
            view.showGenerals(generals)
            // 필터 텍스트 표시
            showFilterText()
        } else {
            // 해당 필터 유형에 대한 작업이 없음 때 호출한다.
            processEmptyTasks()
        }
    }

    private fun processEmptyTasks() {
        when (currentFiltering) {
            GeneralFilterType.MARK_GENERALS -> {

            }
            else -> view.showNoGenerals()
        }
    }

    private fun showFilterText() {
        when (currentFiltering) {
            GeneralFilterType.MARK_GENERALS -> view.showMarkGeneralsFilterText()
            else -> view.showAllGeneralsFilterText()
        }
    }

    override fun addGeneral() {
        TODO("Not yet implemented")
    }

    override fun openGeneral(requestGeneral: General) {
        TODO("Not yet implemented")
    }
}