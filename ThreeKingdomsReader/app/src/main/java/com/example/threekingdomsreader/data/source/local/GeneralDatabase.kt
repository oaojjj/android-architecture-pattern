package com.example.threekingdomsreader.data.source.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.threekingdomsreader.data.General
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlin.concurrent.thread

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
                )/*.addCallback(object : Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        thread {
                            getInstance(context).GeneralDao()
                                .addGenerals(getJsonDataFromAssets(context))
                        }
                    }
                })*/
                    .createFromAsset("database/Generals.db").build().apply { INSTANCE = this }
            }
        }

        fun getJsonDataFromAssets(context: Context): List<General> {
            val jsonString = context.assets.open("generals.json")
                .bufferedReader()
                .use { it.readText() }

            return Gson().fromJson(jsonString, object : TypeToken<List<General>>() {}.type)
        }
    }
}