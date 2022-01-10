package com.oaojjj.architecturepattern.data.source

class TodoRepository(val todoLocalDataSource: TodoDataSource) : TodoDataSource {

    companion object {
        private var INSTANCE: TodoRepository? = null

        /**
         * Returns the single instance of this class, creating it if necessary.
         * *
         * @param todoLocalDataSource  the device storage data source
         * *
         * @return the [TodoRepository] instance
         */
        @JvmStatic
        fun getInstance(todoLocalDataSource: TodoDataSource): TodoRepository {
            return INSTANCE ?: TodoRepository(todoLocalDataSource).apply { INSTANCE = this }
        }

        /**
         * Used to force [getInstance] to create a new instance
         * next time it's called.
         */
        @JvmStatic fun destroyInstance() {
            INSTANCE = null
        }
    }
}