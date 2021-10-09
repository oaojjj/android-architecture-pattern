package com.oaojjj.architecturepattern.customview

import android.graphics.*
import android.graphics.drawable.Drawable
import android.text.TextPaint
import android.util.Log
import com.oaojjj.architecturepattern.listener.OnUnderlayButtonClickListener


class UnderlayButton() {
    private var mText: String = "기능"
    private var mTextColor: Int = Color.WHITE
    private var mTextSize: Float = 64f

    private var mBackground: Int = 0
    private var mImageResId: Drawable? = null

    private var mPos: Int = -1
    private var mAnimate: Boolean = false

    private val r = Rect()

    private lateinit var paint: Paint
    private lateinit var textPaint: TextPaint

    private var mClickRegion: RectF? = null
    private var mListener: OnUnderlayButtonClickListener? = null

    constructor(
        text: String,
        imageResId: Drawable? = null,
        background: Int,
        textColor: Int = Color.WHITE,
        listener: OnUnderlayButtonClickListener? = null
    ) : this() {
        mText = text
        mImageResId = imageResId
        mBackground = background
        mTextColor = textColor
        mListener = listener

        paint = Paint().apply { color = mBackground }
        textPaint = TextPaint().apply {
            color = mTextColor
            textSize = mTextSize
            textAlign = Paint.Align.LEFT
            getTextBounds(mText, 0, mText.length, r)
        }
    }

    fun onDraw(canvas: Canvas, rectF: RectF, pos: Int) {
        Log.d("SwipeController", "onDraw: rectf_$rectF, pos_$pos")

        canvas.apply {
            drawRect(rectF, paint)

            val x = rectF.width() / 2f - r.width() / 2f - r.left
            val y = rectF.height() / 2f + r.height() / 2f - r.bottom
            drawText(mText, rectF.left + x, rectF.top + y, textPaint)
        }.save()

        // drawImage(rectF, canvas)

        /*if (mAnimate) {
            canvas.save()

            val r = Rect()
            val y: Float =
                rectF.height() / 2f + r.height() / 2f - r.bottom - staticLayout.height / 2

            if (mImageResId == null) canvas.translate(rectF.left, rectF.top + y)
            else canvas.translate(rectF.left, rectF.top + y - 30)

            canvas.restore()
        }*/
        mClickRegion = rectF
        mPos = pos
    }

    private fun drawImage(rectF: RectF, canvas: Canvas) {
        mImageResId?.apply {
            setBounds(
                ((rectF.left + 50).toInt()),
                ((rectF.top + (rectF.height() / 2f)).toInt()),
                ((rectF.right - 50).toInt()),
                ((rectF.bottom - ((rectF.height() / 10f))).toInt())
            )
        }?.draw(canvas)
    }

    fun onClick(x: Float, y: Float): Boolean {
        if (mClickRegion != null && mClickRegion?.contains(x, y) == true) {
            mListener?.onUnderlayButtonClick(mPos)
            return true
        }
        return false
    }

}