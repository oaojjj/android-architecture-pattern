package com.oaojjj.architecturepattern.util

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
import com.oaojjj.architecturepattern.todos.UnderlayButton
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener


@SuppressLint("ClickableViewAccessibility")
abstract class SwipeHelper(context: Context, var mRecyclerView: RecyclerView) :
    ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
    private var buttonWidth = context.resources.getDimension(R.dimen.underlay_button_width)
    private var buttons: MutableList<UnderlayButton> = mutableListOf()

    private val buttonsBuffer: MutableMap<Int, MutableList<UnderlayButton>> = mutableMapOf()
    private lateinit var recoverQueue: Queue<Int?>

    private var swipedPos = -1
    private var swipeThreshold = 0.5f

    /**
     * GestureDetector
     * rect 사용자 터치 감지
     */
    private lateinit var gestureDetector: GestureDetector
    private val gestureListener: GestureDetector.OnGestureListener =
        object : GestureDetector.SimpleOnGestureListener() {
            override fun onSingleTapConfirmed(e: MotionEvent?): Boolean {
                var count = 0
                Log.d(TAG, "gestureListener_test: ${e?.action}, x:${e?.x}, y:${e?.y}")
                e?.let {
                    for (button in buttons) {
                        count += 1
                        if (button.onClick(e.x, e.y)) {
                            swipedPos = if (count == 1) {
                                -1
                            } else {
                                recoverSwipedItem()
                                -1
                            }
                            Log.d(TAG, "onSingleTapConfirmed: $recoverQueue")
                            break
                        }
                    }
                }
                return true
            }
        }

    /**
     * RecyclerView TouchListener
     */
    private val onTouchListener = OnTouchListener { _, event ->
        if (swipedPos < 0) return@OnTouchListener false
        Log.d("TAG", "onTouchListener: ")
        // 현재 touch가 발생한 위치
        val point = Point(event.rawX.toInt(), event.rawY.toInt())
        val rect = Rect()
        // 뷰가 보이면 true 반환 + rect 위치 추적하는듯? -> rect 포지션을 위해 호출하는 듯
        mRecyclerView.findViewHolderForAdapterPosition(swipedPos)
            ?.itemView?.getGlobalVisibleRect(rect)


        if (event.action == MotionEvent.ACTION_DOWN
            || event.action == MotionEvent.ACTION_UP
            || event.action == MotionEvent.ACTION_MOVE
        ) {
            // 해당하는 pos에 touch이벤트 발생
            if (rect.top < point.y && rect.bottom > point.y) {
                Log.d("onTouchListener", "swipedPos:${swipedPos}")
                gestureDetector.onTouchEvent(event)
            } else {
                Log.d("onTouchListener", "else")
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


    init {
        gestureDetector = GestureDetector(context, gestureListener)
        mRecyclerView.setOnTouchListener(onTouchListener)
        recoverQueue = object : LinkedList<Int?>() {
            override fun add(element: Int?): Boolean {
                return if (contains(element)) false else super.add(element)
            }
        }
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }

    // swipe 완료 시점에 호출
    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val pos = viewHolder.adapterPosition
        Log.d(Companion.TAG, "onSwiped: swipedPos:$swipedPos, pos:$pos, buttonsBuffer:$buttonsBuffer")

        if (swipedPos != pos) {
            recoverQueue.add(pos)
        }

        swipedPos = pos

        if (buttonsBuffer.containsKey(swipedPos)) buttons = buttonsBuffer[swipedPos]!!
        else buttons.clear()

        buttonsBuffer.clear()
        swipeThreshold = 0.5f * buttons.size * buttonWidth
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

        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            if (dX < 0) {
                var buffer: MutableList<UnderlayButton> = mutableListOf()

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
        var right = v.right.toFloat()
        val dButtonWidth = (-1) * dX / buffer.size

        for (button in buffer) {
            val left = right - dButtonWidth
            button.onDraw(c, RectF(left, v.top.toFloat(), right, v.bottom.toFloat()), pos)
            right = left
        }
    }

    @Synchronized
    private fun recoverSwipedItem() {
        Log.d(TAG, "recoverSwipedItem: $recoverQueue")
        while (!recoverQueue.isEmpty()) {
            val pos = recoverQueue.poll() ?: -1
            if (pos > -1) mRecyclerView.adapter?.notifyItemChanged(pos)
        }
    }

    companion object {
        private const val TAG: String = "SwipeController"
    }

}