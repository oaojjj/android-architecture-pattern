package com.oaojjj.architecturepattern.model

import androidx.room.*

@Dao
interface TodoDao {
    @Query("SELECT * FROM Todo")
    fun getAll(): MutableList<Todo>

    @Query("SELECT * FROM Todo WHERE content = :content")
    fun getTodo(content: String): Todo

    @Insert
    fun insert(todo: Todo)

    @Update
    fun update(todo: Todo)

    @Delete
    fun delete(todo: Todo)

    @Query("Update Todo SET checked = :checked WHERE id = :id")
    fun updateChecked(id: Int, checked: Boolean)
}