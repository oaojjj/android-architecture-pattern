package com.oaojjj.architecturepattern.util

import android.content.Context
import com.oaojjj.architecturepattern.data.source.TodoRepository
import com.oaojjj.architecturepattern.data.source.local.TodoDatabase
import com.oaojjj.architecturepattern.data.source.local.TodoLocalDataSource

object Injection {
    fun provideTodoRepository(context: Context): TodoRepository {
        val database = TodoDatabase.getInstance(context)
        return TodoRepository.getInstance(
            TodoLocalDataSource.getInstance(AppExecutors(), database.todoDao())
        )
    }
}