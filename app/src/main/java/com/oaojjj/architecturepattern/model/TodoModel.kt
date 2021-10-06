package com.oaojjj.architecturepattern.model

import android.content.Context
import android.util.Log

// model
class TodoModel(mContext: Context) {
    private var mDataList: MutableList<Todo>
    private var mPosition: Int = 0

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

    fun setPosition(position: Int) {
        mPosition = position
    }

    fun getPosition(): Int = mPosition

    fun addTodo(newContents: String) {
        mDataList.add(Todo(newContents))
        db.insert(mDataList.last())
    }

    fun removeTodo() {
        db.delete(mDataList[mPosition])
        mDataList.removeAt(mPosition)
    }

    fun updateTodo(newContents: String) {
        mDataList[mPosition].content = newContents
        db.update(mDataList[mPosition])
    }

    fun updateChecked(position: Int, checked: Boolean) {
        mDataList[position].checked = checked
        db.updateChecked(mDataList[position].id, checked)
    }

    fun size(): Int = mDataList.size
}