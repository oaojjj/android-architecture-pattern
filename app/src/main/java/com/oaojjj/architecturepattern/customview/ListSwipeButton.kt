package com.oaojjj.architecturepattern.customview

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import androidx.recyclerview.widget.RecyclerView

class ListSwipeButton(var c: Canvas, var vh: RecyclerView.ViewHolder) : RectF() {
    companion object {
        const val BUTTON_WIDTH = 300f
        const val BUTTON_WIDTH_WITHOUT_PADDING = BUTTON_WIDTH - 20
        const val CORNERS = 16f
    }

    fun drawRoundRect(color: Int, state: Int, left: Float, right: Float) {
        set(state - left, vh.itemView.top.toFloat(), state + right, vh.itemView.bottom.toFloat())
        c.drawRoundRect(this, CORNERS, CORNERS, Paint().apply { this.color = color })
    }

    fun drawText(s: String) {
        val paint = Paint().apply {
            color = Color.WHITE
            isAntiAlias = true
            textSize = 60f
        }

        c.drawText(
            s,
            this.centerX() - paint.measureText(s) / 2,
            this.centerY() + paint.textSize / 2,
            paint
        )
    }


}
