package com.benjaminwan.epoxyswipedemo.utils

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

fun Context.dp2px(value: Int): Int {
    val scale = resources.displayMetrics.density
    return (value.toFloat() * scale + 0.5f).toInt()
}

fun RecyclerView.setItemDecoration(left: Int, top: Int, right: Int, bottom: Int) {
    addItemDecoration(object : RecyclerView.ItemDecoration() {
        override fun getItemOffsets(
            outRect: Rect,
            view: View,
            parent: RecyclerView,
            state: RecyclerView.State
        ) {
            //设置item间距
            outRect.left = context.dp2px(left)
            outRect.top = context.dp2px(top)
            outRect.right = context.dp2px(right)
            outRect.bottom = context.dp2px(bottom)
        }
    })
}

/**
 * 设置RecyclerView的item四周间距，并在item底部画分隔符
 * @param  left 左边距
 * @param  top 上边距
 * @param  right 右边距
 * @param  bottom 底边距
 */
fun RecyclerView.setItemDecorationAndDrawBottomSeparator(
    left: Int, top: Int, right: Int, bottom: Int,
    bottomLine: Int
) {
    addItemDecoration(object : RecyclerView.ItemDecoration() {
        override fun getItemOffsets(
            outRect: Rect,
            view: View,
            parent: RecyclerView,
            state: RecyclerView.State
        ) {
            //设置item间距
            outRect.left = context.dp2px(left)
            outRect.top = context.dp2px(top)
            outRect.right = context.dp2px(right)
            outRect.bottom = context.dp2px(bottom)
        }

        override fun onDraw(canvas: Canvas, parent: RecyclerView, state: RecyclerView.State) {
            super.onDraw(canvas, parent, state)
            //分隔符
            val paint = Paint().apply {
                color = Color.LTGRAY
            }

            val left = parent.paddingLeft //不可以修改left等变量名，否则item底色会变成灰色
            val right = parent.measuredWidth - parent.paddingRight
            val childSize = parent.childCount
            for (i in 0 until childSize) {
                val child = parent.getChildAt(i)
                val layoutParams = child.layoutParams as RecyclerView.LayoutParams
                val top = child.bottom + layoutParams.bottomMargin
                val bottom = top + context.dp2px(bottomLine)
                canvas.drawRect(
                    left.toFloat(), top.toFloat(),
                    right.toFloat(), bottom.toFloat(), paint
                )
            }
        }
    })
}

fun RecyclerView.scrollToTop() {
    val layoutManager = this.layoutManager as LinearLayoutManager?
    layoutManager?.scrollToPositionWithOffset(0, 0)
}

fun RecyclerView.scrollToBottom() {
    val layoutManager = this.layoutManager as LinearLayoutManager?
    val itemCount = this.adapter?.itemCount ?: 0
    layoutManager?.scrollToPositionWithOffset(itemCount - 1, 0)
}