package com.oaojjj.architecturepattern.controller

import android.annotation.SuppressLint
import android.graphics.Canvas
import android.graphics.Color
import android.util.Log
import android.view.MotionEvent
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ItemTouchHelper.*

import androidx.recyclerview.widget.RecyclerView
import com.oaojjj.architecturepattern.enum.ListSwipeState

import com.oaojjj.architecturepattern.customview.ListSwipeButton


class SwipeController : ItemTouchHelper.Callback() {
    private var swipeState = ListSwipeState.GONE
    private var swipeFlag: Boolean = false
    private var buttonInstance: ListSwipeButton? = null

    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        return makeMovementFlags(0, LEFT or RIGHT)
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        // do something
    }

    // block swipe
    override fun convertToAbsoluteDirection(flags: Int, layoutDirection: Int): Int {
        Log.d("method_test", "convertToAbsoluteDirection: $flags,$layoutDirection")
        if (swipeFlag) {
            swipeFlag = false
            return 0
        }
        return super.convertToAbsoluteDirection(flags, layoutDirection)
    }

    // 아이템을 터치하거나 스와이프와 같이 뷰에 변화가 생길경우 호출되는 메소드
    override fun onChildDraw(
        c: Canvas,
        rv: RecyclerView,
        vh: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        if (actionState == ACTION_STATE_SWIPE)
            setTouchListener(c, rv, vh, dX, dY, actionState, isCurrentlyActive)

        drawButtons(c, vh)
        super.onChildDraw(c, rv, vh, dX, dY, actionState, isCurrentlyActive)
    }

    // 양쪽의 버튼(수정 ,삭제) 그려줌
    private fun drawButtons(c: Canvas, vh: RecyclerView.ViewHolder) {
        Log.d("test", "drawButtons: $swipeState")
        val width = ListSwipeButton.BUTTON_WIDTH_WITHOUT_PADDING
        val lb =
            ListSwipeButton(c, vh).apply { drawRoundRect(Color.BLUE, vh.itemView.left, 0f, width) }
        val rb =
            ListSwipeButton(c, vh).apply { drawRoundRect(Color.RED, vh.itemView.right, width, 0f) }
        buttonInstance = when (swipeState) {
            ListSwipeState.LEFT_VISIBLE -> lb
            ListSwipeState.RIGHT_VISIBLE -> rb
            else -> null
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setTouchListener(
        c: Canvas,
        rv: RecyclerView,
        vh: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        rv.setOnTouchListener { _, event ->
            Log.d("motion_test", "setTouchListener: ${event.action}")
            // 스와이프 도중에 손을 떼는등의 취소가 되면 swipe block
            swipeFlag =
                event.action == MotionEvent.ACTION_CANCEL || event.action == MotionEvent.ACTION_UP
            if (swipeFlag) {
                when {
                    dX < -ListSwipeButton.BUTTON_WIDTH -> swipeState = ListSwipeState.RIGHT_VISIBLE
                    dX > ListSwipeButton.BUTTON_WIDTH -> swipeState = ListSwipeState.LEFT_VISIBLE
                    swipeState != ListSwipeState.GONE -> {
                        setTouchDownListener(c, rv, vh, dX, dY, actionState, isCurrentlyActive)
                        setItemsClickable(rv, false)
                    }
                }
            }
            false
        }
    }

    /**
     * set touchListener, clickable
     * : 리사이클러뷰에 기존의 setOnClickListener 가 존재한다면 이벤트가 겹치기 때문에..? 해주는거 같음
     */

    @SuppressLint("ClickableViewAccessibility")
    private fun setTouchDownListener(
        c: Canvas,
        rv: RecyclerView,
        vh: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        rv.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                Log.d("touch_test", "setTouchDownListener call")
                setTouchUpListener(c, rv, vh, dX, dY, actionState, isCurrentlyActive)
            }
            false
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setTouchUpListener(
        c: Canvas,
        rv: RecyclerView,
        vh: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        rv.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                Log.d("touch_test", "setTouchUpListener call")
                super.onChildDraw(c, rv, vh, 0f, dY, actionState, isCurrentlyActive)
                rv.setOnTouchListener { _, _ -> false }
                setItemsClickable(rv, true)
                swipeFlag = false
                swipeState = ListSwipeState.GONE
            }
            false
        }
    }

    private fun setItemsClickable(recyclerView: RecyclerView, isClickable: Boolean) {
        for (i in 0 until recyclerView.childCount) {
            recyclerView.getChildAt(i).isClickable = isClickable
        }
    }


}