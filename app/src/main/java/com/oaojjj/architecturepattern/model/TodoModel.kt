package com.oaojjj.architecturepattern.model

import android.content.Context
import androidx.room.Room

// model
class TodoModel(mContext: Context) {
    private val db: TodoDatabase = TodoDatabase.getInstance(mContext)
    fun db(): TodoDatabase = db
}