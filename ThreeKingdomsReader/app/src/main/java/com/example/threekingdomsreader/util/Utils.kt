package com.example.threekingdomsreader.util

import android.content.Context
import android.view.View
import androidx.core.content.ContextCompat
import com.example.threekingdomsreader.R

object Utils {
    fun getColorAccordingBelong(context: Context, belong: String): Int {
        return when (belong) {
            "위" -> ContextCompat.getColor(context, R.color.wei)
            "촉" -> ContextCompat.getColor(context, R.color.shu)
            "오" -> ContextCompat.getColor(context, R.color.wu)
            else -> ContextCompat.getColor(context, R.color.grey_200)
        }
    }
}