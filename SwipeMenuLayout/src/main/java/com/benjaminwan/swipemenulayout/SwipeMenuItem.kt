package com.benjaminwan.swipemenulayout

import android.graphics.Typeface
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StyleRes

data class SwipeMenuItem @JvmOverloads constructor(
    var id: Int = 0,
    @DrawableRes var backgroundRes: Int = 0,
    @DrawableRes var iconRes: Int = 0,
    @ColorRes var iconColorRes: Int = 0,
    var title: CharSequence = "",
    @ColorRes var titleColorRes: Int = 0,
    var titleSize: Int = 10,
    var textTypeface: Typeface? = null,
    var textAppearance: Int = 0,
    var width: Int = -2,
    var height: Int = -2,
    var weight: Int = 0,
    var tag: Any? = null
) {

    class Builder {
        val item = SwipeMenuItem()

        fun setId(id: Int): Builder {
            item.id = id
            return this
        }

        fun setBackground(@DrawableRes idRes: Int): Builder {
            item.backgroundRes = idRes
            return this
        }

        fun setTitle(title: CharSequence): Builder {
            item.title = title
            return this
        }

        fun setTitleSize(size: Int): Builder {
            item.titleSize = size
            return this
        }

        fun setTitleColor(@ColorRes idRes: Int): Builder {
            item.titleColorRes = idRes
            return this
        }

        fun setIcon(@DrawableRes idRes: Int): Builder {
            item.iconRes = idRes
            return this
        }

        fun setIconColor(@ColorRes idRes: Int): Builder {
            item.iconColorRes = idRes
            return this
        }

        fun setWidth(width: Int): Builder {
            item.width = width
            return this
        }

        fun setHeight(height: Int): Builder {
            item.height = height
            return this
        }

        fun setTextTypeface(textTypeface: Typeface): Builder {
            item.textTypeface = textTypeface
            return this
        }

        fun setTextAppearance(@StyleRes textAppearance: Int): Builder {
            item.textAppearance = textAppearance
            return this
        }

        fun setTag(tag: Any): Builder {
            item.tag = tag
            return this
        }

        fun build(): SwipeMenuItem {
            return item
        }
    }
}

@DslMarker
annotation class SwipeItemDsl

@SwipeItemDsl
class SwipeMenuItemDslBuilder {
    var id: Int = 0

    @DrawableRes
    var backgroundRes: Int = 0

    @DrawableRes
    var iconRes: Int = 0

    @ColorRes
    var iconColorRes: Int = 0
    var title: CharSequence = ""

    @ColorRes
    var titleColorRes: Int = 0
    var titleSize: Int = 10
    var textTypeface: Typeface? = null
    var textAppearance: Int = 0
    var width: Int = -2
    var height: Int = -2
    var weight: Int = 0
    var tag: Any? = null
    fun build(): SwipeMenuItem =
        SwipeMenuItem(
            id,
            backgroundRes,
            iconRes,
            iconColorRes,
            title,
            titleColorRes,
            titleSize,
            textTypeface,
            textAppearance,
            width,
            height,
            weight,
            tag
        )
}

@SwipeItemDsl
class ListItemConfigAdder : ArrayList<SwipeMenuItem>() {
    inline fun menuItem(block: SwipeMenuItemDslBuilder.() -> Unit) {
        add(SwipeMenuItemDslBuilder().apply(block).build())
    }

    fun menuItem(config: SwipeMenuItem) {
        add(config)
    }

    fun listOfMenuItems(configs: List<SwipeMenuItem>) {
        addAll(configs)
    }

}

inline fun menuItems(block: ListItemConfigAdder.() -> Unit): List<SwipeMenuItem> =
    ListItemConfigAdder().apply(block)