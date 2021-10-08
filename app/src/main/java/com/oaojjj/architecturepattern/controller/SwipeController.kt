package com.oaojjj.architecturepattern.controller

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Point
import android.graphics.Rect
import android.graphics.RectF
import android.util.Log
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.oaojjj.architecturepattern.R
import java.util.*
import com.oaojjj.architecturepattern.customview.UnderlayButton
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener


@SuppressLint("ClickableViewAccessibility")
abstract class SwipeController(context: Context, var recyclerView: RecyclerView) :
    ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
    private val TAG: String = "SwipeController"
    private var buttonWidth = context.resources.getDimension(R.dimen.underlay_button_width)
    private var buttons: MutableList<UnderlayButton> = mutableListOf()

    private val buttonsBuffer: MutableMap<Int, MutableList<UnderlayButton>> = mutableMapOf()
    private lateinit var recoverQueue: Queue<Int?>

    private var swipedPos = -1
    private var swipeThreshold = 0.5f

    /**
     * GestureDetector
     * 사용자 터치 감지
     */
    private lateinit var gestureDetector: GestureDetector
    private val gestureListener: GestureDetector.OnGestureListener =
        object : GestureDetector.SimpleOnGestureListener() {
            override fun onSingleTapConfirmed(e: MotionEvent?): Boolean {
                Log.d(TAG, "onSingleTapConfirmed: ")
                //for (button in buttons)
                //if (button.onClick(e!!.x, e.y)) break
                return true
            }
        }

    /**
     * RecyclerView TouchListener
     */
    private val onTouchListener = OnTouchListener { _, event ->
        Log.d(TAG, "onTouchListener: ")
        if (swipedPos < 0) return@OnTouchListener false
        val point = Point(event.rawX.toInt(), event.rawY.toInt())
        val rect = Rect()

        recyclerView.findViewHolderForAdapterPosition(swipedPos)
            ?.itemView?.getGlobalVisibleRect(rect)
        if (event.action == MotionEvent.ACTION_DOWN
            || event.action == MotionEvent.ACTION_UP
            || event.action == MotionEvent.ACTION_MOVE
        ) {
            if (rect.top < point.y && rect.bottom > point.y) gestureDetector.onTouchEvent(event)
            else {
                recoverQueue.add(swipedPos)
                swipedPos = -1
                recoverSwipedItem()
            }
        }
        false
    }


    /**
     * create(add to itemView) underlayButton
     * 추상 메소드를 이용해서 버튼을 만듬
     */
    abstract fun instantiateUnderlayButton(
        vh: RecyclerView.ViewHolder,
        buttons: MutableList<UnderlayButton>
    )


    /**
     * 초기화
     */
    init {
        gestureDetector = GestureDetector(context, gestureListener)
        recyclerView.setOnTouchListener(onTouchListener)
        recoverQueue = object : LinkedList<Int?>() {
            override fun add(e: Int?): Boolean {
                return if (contains(e)) false else super.add(e)
            }
        }
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        Log.d(TAG, "onMove")
        return false
    }

    // swipe 완료 시점에 호출
    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val pos = viewHolder.adapterPosition

        if (swipedPos != pos) recoverQueue.add(swipedPos)

        swipedPos = pos

        if (buttonsBuffer.containsKey(swipedPos)) buttons = buttonsBuffer[swipedPos]!!
        else buttons.clear()

        buttonsBuffer.clear()
        swipeThreshold = 0.5f * buttons.size * buttonWidth
        recoverSwipedItem()
    }

    override fun getSwipeThreshold(viewHolder: RecyclerView.ViewHolder): Float {
        return swipeThreshold
    }

    override fun getSwipeEscapeVelocity(defaultValue: Float): Float {
        return 0.1f * defaultValue
    }

    override fun getSwipeVelocityThreshold(defaultValue: Float): Float {
        return 5.0f * defaultValue
    }

    override fun onChildDraw(
        c: Canvas,
        rv: RecyclerView,
        vh: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        val pos = vh.adapterPosition
        var translationX = dX
        val itemView = vh.itemView

        Log.d(TAG, "onChildDraw: pos_$pos")
        if (pos < 0) {
            Log.d(TAG, "onChildDraw: swipedPos_$swipedPos")
            swipedPos = pos
            return
        }

        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            Log.d(TAG, "onChildDraw: dx_$dX")
            if (dX < 0) {
                var buffer: MutableList<UnderlayButton> = mutableListOf()

                Log.d(TAG, "onChildDraw: buffer_$buffer")
                if (!buttonsBuffer.containsKey(pos)) {
                    instantiateUnderlayButton(vh, buffer)
                    buttonsBuffer[pos] = buffer
                } else buffer = buttonsBuffer[pos]!!

                translationX = dX * buffer.size * buttonWidth / itemView.width
                drawButtons(c, itemView, buffer, pos, translationX)
            }
        }

        super.onChildDraw(c, rv, vh, translationX, dY, actionState, isCurrentlyActive)
    }

    private fun drawButtons(c: Canvas, v: View, buffer: List<UnderlayButton>, pos: Int, dX: Float) {
        Log.d(TAG, "drawButtons: $dX, buffer_$buffer")
        var right = v.right.toFloat()
        val dButtonWidth = (-1) * dX / buffer.size

        for (button in buffer) {
            val left = right - dButtonWidth
            Log.d(TAG, "drawButtons: left_$left, right_$right")
            button.onDraw(c, RectF(left, v.top.toFloat(), right, v.bottom.toFloat()), pos)
            right = left
        }
    }

    @Synchronized
    private fun recoverSwipedItem() {
        while (!recoverQueue.isEmpty()) {
            val pos = recoverQueue.poll()
            if (pos!! > -1) {
                recyclerView.adapter?.notifyItemChanged(pos)
            }
        }
    }


}