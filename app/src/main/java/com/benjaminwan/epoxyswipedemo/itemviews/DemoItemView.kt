package com.benjaminwan.epoxyswipedemo.itemviews

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.airbnb.epoxy.ModelView
import com.airbnb.epoxy.TextProp
import com.benjaminwan.epoxyswipedemo.R

@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_WRAP_HEIGHT)
class DemoItemView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private val demoTv by lazy { findViewById<TextView>(R.id.demoTv) }

    init {
        View.inflate(context, R.layout.rv_demo_item, this)
    }

    @TextProp
    fun setDemo(content: CharSequence) {
        demoTv.text = content
    }
}