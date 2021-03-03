package com.benjaminwan.epoxyswipedemo.menu

import android.view.ViewGroup
import androidx.annotation.StringRes
import com.benjaminwan.epoxyswipedemo.R
import com.benjaminwan.epoxyswipedemo.app.App
import com.benjaminwan.swipemenulayout.SwipeMenuItem
import com.benjaminwan.swipemenulayout.menuItems

fun SwipeMenuItem.Builder.setTitle(@StringRes idRes: Int): SwipeMenuItem.Builder =
    this.setTitle(App.instance.getString(idRes))

fun getString(@StringRes idRes: Int): String = App.instance.getString(idRes)

val leftMenus: List<SwipeMenuItem> =
    menuItems {
        menuItem {
            id = 0
            width = 60
            height = ViewGroup.LayoutParams.MATCH_PARENT
            title = getString(R.string.left_delete_title)
            titleSize = 10
            titleColorRes = R.color.selector_white_to_grey5
            backgroundRes = R.drawable.bg_orange5_to_orange7
            iconRes = R.drawable.ic_delete
            iconColorRes = R.color.selector_white_to_grey5
        }
        menuItem {
            id = 1
            width = 80
            height = ViewGroup.LayoutParams.MATCH_PARENT
            title = "Left Clear"
            titleSize = 11
            titleColorRes = R.color.selector_white_to_grey5
            backgroundRes = R.drawable.bg_red5_to_red7
            iconRes = R.drawable.ic_clear
            iconColorRes = R.color.selector_white_to_grey5
        }
    }

val rightMenus: List<SwipeMenuItem> =
    listOf(
        SwipeMenuItem.Builder()
            .setId(10)
            .setWidth(60)
            .setHeight(ViewGroup.LayoutParams.MATCH_PARENT)
            .setTitle(R.string.right_delete_title)
            .setTitleSize(10)
            .setTitleColor(R.color.selector_white_to_grey5)
            .setBackground(R.drawable.bg_red5_to_red7)
            .setIcon(R.drawable.ic_delete)
            .setIconColor(R.color.selector_white_to_grey5)
            .build(),
        SwipeMenuItem.Builder()
            .setId(11)
            .setWidth(80)
            .setHeight(ViewGroup.LayoutParams.MATCH_PARENT)
            .setTitle("Right Restore")
            .setTitleSize(11)
            .setTitleColor(R.color.selector_white_to_grey5)
            .setBackground(R.drawable.bg_green5_to_green7)
            .setIcon(R.drawable.ic_restore)
            .setIconColor(R.color.selector_white_to_grey5)
            .build()
    )