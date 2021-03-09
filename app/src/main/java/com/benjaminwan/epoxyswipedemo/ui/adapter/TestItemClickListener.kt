package com.benjaminwan.epoxyswipedemo.ui.adapter

import android.view.View
import com.benjaminwan.swipemenulayout.SwipeMenuItem
import com.benjaminwan.swipemenulayout.SwipeMenuLayout

interface TestItemClickListener {
    fun onItemClick(swipeMenuLayout: SwipeMenuLayout, view: View, itemContent: String)
    fun onMenuItemClick(
        swipeMenuLayout: SwipeMenuLayout,
        itemContent: String,
        swipeMenuItem: SwipeMenuItem
    )
}