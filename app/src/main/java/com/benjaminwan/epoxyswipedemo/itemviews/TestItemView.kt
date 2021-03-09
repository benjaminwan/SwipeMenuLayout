package com.benjaminwan.epoxyswipedemo.itemviews

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.airbnb.epoxy.CallbackProp
import com.airbnb.epoxy.ModelProp
import com.airbnb.epoxy.ModelView
import com.airbnb.epoxy.TextProp
import com.benjaminwan.epoxyswipedemo.R
import com.benjaminwan.swipemenulayout.*

@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_WRAP_HEIGHT)
class TestItemView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val swipeMenuLayout by lazy { findViewById<SwipeMenuLayout>(R.id.swipeMenuLayout) }
    private val contentLayout: LinearLayout by lazy { findViewById(R.id.contentLayout) }
    private val contentTv by lazy { findViewById<TextView>(R.id.contentTv) }
    private val leftIv by lazy { findViewById<ImageView>(R.id.leftIv) }
    private val rightIv by lazy { findViewById<ImageView>(R.id.rightIv) }

    init {
        View.inflate(context, R.layout.rv_test_include_item, this)
    }

    @TextProp
    fun setContent(content: CharSequence) {
        contentTv.text = content
    }

    @ModelProp
    fun setMenuOpenThreshold(threshold: Float?) {
        threshold ?: return
        swipeMenuLayout.menuOpenThreshold = threshold
    }

    @CallbackProp
    fun onClickListener(listener: ((swipeLayout: SwipeMenuLayout, view: View) -> Unit)?) {
        contentLayout.setOnClickListener {
            listener?.invoke(swipeMenuLayout, contentLayout)
        }
    }

    @ModelProp
    @JvmOverloads
    fun setRightMenu(rightMenu: List<SwipeMenuItem>? = null) {
        if (rightMenu == null || rightMenu.isEmpty()) {
            swipeMenuLayout.rightMenuView.removeAllViews()
            swipeMenuLayout.rightMenuEnable = false
            rightIv.visibility = View.GONE
        } else {
            swipeMenuLayout.rightMenuView.createMenu(rightMenu)
            swipeMenuLayout.rightMenuEnable = true
            rightIv.visibility = View.VISIBLE
        }
    }

    @CallbackProp
    fun onRightMenuClickListener(listener: ((swipeLayout: SwipeMenuLayout, item: SwipeMenuItem) -> Unit)?) {
        swipeMenuLayout.rightMenuView.setOnMenuItemClickListener { item ->
            listener?.invoke(swipeMenuLayout, item)
        }
    }

    @CallbackProp
    fun onRightMenuOpenedListener(listener: OnMenuRightOpenedListener) {
        swipeMenuLayout.addOnMenuRightOpenedListener(listener)
    }

    @ModelProp
    @JvmOverloads
    fun setLeftMenu(leftMenu: List<SwipeMenuItem>? = null) {
        if (leftMenu == null || leftMenu.isEmpty()) {
            swipeMenuLayout.leftMenuView.removeAllViews()
            swipeMenuLayout.leftMenuEnable = false
            leftIv.visibility = View.GONE
        } else {
            swipeMenuLayout.leftMenuView.createMenu(leftMenu)
            swipeMenuLayout.leftMenuEnable = true
            leftIv.visibility = View.VISIBLE
        }
    }

    @CallbackProp
    fun onLeftMenuClickListener(listener: ((swipeLayout: SwipeMenuLayout, item: SwipeMenuItem) -> Unit)?) {
        swipeMenuLayout.leftMenuView.setOnMenuItemClickListener { item ->
            listener?.invoke(swipeMenuLayout, item)
        }
    }

    @CallbackProp
    fun onLeftMenuOpenedListener(listener: OnMenuLeftOpenedListener) {
        swipeMenuLayout.addOnMenuLeftOpenedListener(listener)
    }

    @CallbackProp
    fun onMenuClosedListener(listener: OnMenuClosedListener) {
        swipeMenuLayout.addOnMenuClosedListener(listener)
    }
}