package com.example.threekingdomsreader.util

import android.content.Context
import com.example.threekingdomsreader.data.GeneralsRepository
import com.example.threekingdomsreader.data.source.local.GeneralDatabase
import com.example.threekingdomsreader.data.source.local.GeneralsLocalDataSource

object Injection {
    fun provideTodoRepository(context: Context): GeneralsRepository {
        val database = GeneralDatabase.getInstance(context)
        return GeneralsRepository.getInstance(
            GeneralsLocalDataSource.getInstance(AppExecutors(), database.GeneralDao())
        )
    }
}