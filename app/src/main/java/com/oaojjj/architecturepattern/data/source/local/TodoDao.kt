package com.oaojjj.architecturepattern.data.source.local

import androidx.room.*
import com.oaojjj.architecturepattern.data.Todo

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
    fun updateChecked(id: Long?, checked: Boolean)
}