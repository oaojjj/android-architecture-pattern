package com.oaojjj.architecturepattern.model

import android.content.Context

// model
class TodoModel(mContext: Context) {
    private var mDataList: MutableList<Todo>
    private val db: TodoDao = TodoDatabase.getInstance(mContext).todoDao()

    init {
        mDataList = getAll()
    }

    private fun getAll(): MutableList<Todo> {
        return db.getAll()
    }

    fun getDataList(): MutableList<Todo> {
        return mDataList
    }

    fun addTodo(newContents: String) {
        mDataList.add(Todo(newContents))
        db.insert(mDataList.last())
    }

    fun removeTodo(position: Int) {
        db.delete(mDataList[position])
        mDataList.removeAt(position)
    }

    fun updateTodo(newContents: String, position: Int) {
        mDataList[position].content = newContents
        db.update(mDataList[position])
    }

}