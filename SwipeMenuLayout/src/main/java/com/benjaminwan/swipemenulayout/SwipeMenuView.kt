package com.benjaminwan.swipemenulayout

import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.widget.ImageViewCompat
import androidx.core.widget.TextViewCompat

open class SwipeMenuView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {
    init {
        gravity = Gravity.CENTER_VERTICAL
    }

    private var mItemClickListener: OnMenuItemClickListener = null

    private fun dp2px(value: Int): Int {
        val scale = resources.displayMetrics.density
        return (value.toFloat() * scale + 0.5f).toInt()
    }

    private val SwipeMenuItem.width2Px
        get() = if (width > 0) dp2px(width) else width

    private val SwipeMenuItem.height2Px
        get() = if (height > 0) dp2px(height) else height

    fun setOnMenuItemClickListener(listener: OnMenuItemClickListener) {
        mItemClickListener = listener
    }

    fun createMenu(swipeMenu: List<SwipeMenuItem>?) {
        removeAllViews()
        swipeMenu ?: return
        for ((index, item) in swipeMenu.withIndex()) {
            val params = LayoutParams(item.width2Px, item.height2Px)
            params.weight = item.weight.toFloat()
            val parent = LinearLayout(context)
            parent.id = index
            parent.gravity = Gravity.CENTER
            parent.orientation = VERTICAL
            parent.layoutParams = params
            parent.setOnClickListener {
                mItemClickListener?.invoke(item)
            }
            parent.setBackgroundResource(item.backgroundRes)
            addView(parent)

            if (item.iconRes > 0) {
                val iv = createIcon(item)
                parent.addView(iv)
            }

            val tv = createTitle(item)
            parent.addView(tv)
        }
    }

    private fun createIcon(item: SwipeMenuItem): ImageView {
        val imageView = ImageView(context)
        imageView.setImageResource(item.iconRes)
        ImageViewCompat.setImageTintList(
            imageView,
            ContextCompat.getColorStateList(context, item.iconColorRes)
        )
        return imageView
    }

    private fun createTitle(item: SwipeMenuItem): TextView {
        val textView = TextView(context)
        textView.text = item.title
        textView.gravity = Gravity.CENTER
        val textSize = item.titleSize
        if (textSize > 0) textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize.toFloat())
        val textColor = ContextCompat.getColorStateList(context, item.titleColorRes)
        if (textColor != null) textView.setTextColor(textColor)
        val textAppearance = item.textAppearance
        if (textAppearance != 0) TextViewCompat.setTextAppearance(textView, textAppearance)
        val typeface = item.textTypeface
        if (typeface != null) textView.typeface = typeface
        return textView
    }
}