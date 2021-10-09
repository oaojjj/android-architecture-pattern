package com.oaojjj.architecturepattern.model

import android.content.Context

// model
object TodoModel {

    private var db: TodoDao? = null

    fun setContext(context: Context) {
        db = db ?: TodoDatabase.getInstance(context).todoDao().apply { db = this }
        mDataList = getAll()
    }

    private var mDataList: MutableList<Todo> = mutableListOf()
    private var mPosition: Int = 0

    fun getPosition(): Int = mPosition

    private fun getAll(): MutableList<Todo> = db?.getAll()!!

    fun getDataList(): MutableList<Todo> = mDataList

    fun setPosition(position: Int) {
        mPosition = position
    }

    /**
     * 사용자 이벤트 발생 하고 호출되는 메소드(callback)
     * add, remove, update ...등
     * controller -> Model 에 데이터 추가 요청
     * Model 에서 데이터 조작 후 View 는 UI만 갱신
     */

    fun addTodo(newContents: String) {
        val newTodo: Todo = Todo(newContents).apply {
            id = if (mDataList.isEmpty()) 1 else mDataList.last().id?.plus(1)
        }

        db?.insert(newTodo)
        mDataList.add(newTodo)
    }

    fun removeTodo() {
        db?.delete(mDataList[mPosition])
        mDataList.removeAt(mPosition)
    }

    fun updateTodo(newContents: String) {
        mDataList[mPosition].content = newContents
        db?.update(mDataList[mPosition])
    }

    fun updateChecked(position: Int, checked: Boolean) {
        mDataList[position].checked = checked
        db?.updateChecked(mDataList[position].id, checked)
    }

    fun size(): Int = mDataList.size
}