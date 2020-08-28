package com.oaojjj.architecturepattern.model

import android.content.Context
import androidx.room.Room
import com.oaojjj.architecturepattern.TodoDatabase

// model
class TodoModel(mContext: Context) {
    private val db: TodoDatabase = Room.databaseBuilder(
        mContext,
        TodoDatabase::class.java, "TodoDatabase"
    ).allowMainThreadQueries().build()

    fun db(): TodoDatabase = db
}