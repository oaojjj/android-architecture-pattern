package com.oaojjj.architecturepattern.model

import android.content.Context

// model
object TodoModel {

    private var db: TodoDao? = null

    fun instantiate(context: Context) {
        db = db ?: TodoDatabase.getInstance(context).todoDao().apply { db = this }
        mDataList = getAll()
    }

    private var mDataList: MutableList<Todo> = mutableListOf()
    private var mPosition: Int = 0

    fun getPosition(): Int = mPosition

    fun setPosition(position: Int) {
        mPosition = position
    }

    fun getDataList(): MutableList<Todo> = mDataList

    fun getData(pos: Int) = mDataList[pos]

    fun getAll(): MutableList<Todo> = db?.getAll()!!

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

    fun removeTodo(position: Int) {
        db?.delete(mDataList[position])
        mDataList.removeAt(position)
    }

    fun updateTodo(position: Int, newContents: String) {
        mDataList[position].content = newContents
        db?.update(mDataList[position])
    }

    fun updateChecked(position: Int, checked: Boolean) {
        mDataList[position].checked = checked
        db?.updateChecked(mDataList[position].id, checked)
    }

    fun size(): Int = mDataList.size
}