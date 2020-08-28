package com.oaojjj.architecturepattern.model

import androidx.room.*
import com.oaojjj.architecturepattern.model.Todo

@Dao
interface TodoDao {
    @Query("SELECT * FROM Todo")
    fun getAll(): MutableList<Todo>

    @Insert
    fun insert(todo: Todo)

    @Update
    fun update(todo: Todo)


    @Delete
    fun delete(todo: Todo)
}