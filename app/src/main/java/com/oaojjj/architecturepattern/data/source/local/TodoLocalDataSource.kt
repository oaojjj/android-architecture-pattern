package com.oaojjj.architecturepattern.data.source.local

import androidx.annotation.VisibleForTesting
import com.oaojjj.architecturepattern.data.source.TodoDataSource
import com.oaojjj.architecturepattern.util.AppExecutors

class TodoLocalDataSource(val appExecutors: AppExecutors, val todoDAO: TodoDao) : TodoDataSource {
    companion object {
        private var INSTANCE: TodoLocalDataSource? = null

        @JvmStatic
        fun getInstance(appExecutors: AppExecutors, todoDAO: TodoDao): TodoLocalDataSource {
            if (INSTANCE == null) {
                synchronized(TodoLocalDataSource::javaClass) {
                    INSTANCE = TodoLocalDataSource(appExecutors, todoDAO)
                }
            }
            return INSTANCE!!
        }

        // TODO: 2022-01-10 @VisibleForTesting 알아보기
        @VisibleForTesting
        fun clearInstance() {
            INSTANCE = null
        }
    }
}