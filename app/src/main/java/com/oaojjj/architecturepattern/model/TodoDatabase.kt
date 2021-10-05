package com.oaojjj.architecturepattern.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Todo::class], version = 1)
abstract class TodoDatabase : RoomDatabase() {
    abstract fun todoDao(): TodoDao

    companion object {
        private var INSTANCE: TodoDatabase? = null

        fun getInstance(context: Context): TodoDatabase {
            return INSTANCE ?: Room.databaseBuilder(
                context.applicationContext,
                TodoDatabase::class.java,
                "TodoDatabase"
            ).fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build()
                .apply { INSTANCE = this }
        }
    }
}