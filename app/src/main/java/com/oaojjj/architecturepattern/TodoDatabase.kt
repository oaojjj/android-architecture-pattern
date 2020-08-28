package com.oaojjj.architecturepattern

import androidx.room.Database
import androidx.room.RoomDatabase
import com.oaojjj.architecturepattern.model.Todo
import com.oaojjj.architecturepattern.model.TodoDao

@Database(entities = [Todo::class], version = 1)
abstract class TodoDatabase : RoomDatabase() {
    abstract fun todoDao(): TodoDao
}