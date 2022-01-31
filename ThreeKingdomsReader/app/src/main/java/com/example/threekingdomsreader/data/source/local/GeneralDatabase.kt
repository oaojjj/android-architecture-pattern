package com.example.threekingdomsreader.data.source.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.threekingdomsreader.data.General
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

@Database(entities = [General::class], version = 1)
abstract class GeneralDatabase : RoomDatabase() {

    abstract fun GeneralDao(): GeneralDao

    companion object {

        private var INSTANCE: GeneralDatabase? = null

        fun getInstance(context: Context): GeneralDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    GeneralDatabase::class.java,
                    "Generals.db"
                ).createFromAsset("database/Generals.db")
                    .build().apply { INSTANCE = this }
            }
        }
    }
}