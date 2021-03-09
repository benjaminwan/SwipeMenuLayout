package com.benjaminwan.epoxyswipedemo.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import com.benjaminwan.epoxyswipedemo.R
import com.benjaminwan.epoxyswipedemo.databinding.FragmentSimpleBinding
import com.benjaminwan.epoxyswipedemo.menu.leftMenus
import com.benjaminwan.epoxyswipedemo.menu.rightMenus
import com.benjaminwan.epoxyswipedemo.utils.showToast
import com.benjaminwan.epoxyswipedemo.utils.viewBinding

class SimpleFragment(@LayoutRes contentLayoutId: Int = R.layout.fragment_simple) :
    Fragment(contentLayoutId) {

    private val binding: FragmentSimpleBinding by viewBinding()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private fun initViews() {
        binding.simpleSwipeLayout.leftMenuView.createMenu(leftMenus)
        binding.simpleSwipeLayout.rightMenuView.createMenu(rightMenus)
        binding.contentLayout.setOnClickListener {
            showToast("Click: Simple")
        }
        binding.simpleSwipeLayout.addOnMenuClosedListener {
            binding.leftIv.setImageResource(R.drawable.ic_right)
            binding.rightIv.setImageResource(R.drawable.ic_left)
        }
        binding.simpleSwipeLayout.addOnMenuLeftOpenedListener {
            binding.leftIv.setImageResource(R.drawable.ic_left)
        }
        binding.simpleSwipeLayout.addOnMenuRightOpenedListener {
            binding.rightIv.setImageResource(R.drawable.ic_right)
        }
        binding.simpleSwipeLayout.leftMenuView.setOnMenuItemClickListener {
            showToast("click left: ${it.title}")
            binding.simpleSwipeLayout.closeMenu()
        }
        binding.simpleSwipeLayout.rightMenuView.setOnMenuItemClickListener {
            showToast("click right: ${it.title}")
            binding.simpleSwipeLayout.closeMenu()
        }
        //打开菜单滑动距离门限.默认:0.3f
        //Open menu sliding distance threshold. default: 0.3f
        binding.simpleSwipeLayout.menuOpenThreshold = 0.3f
        //左菜单使能 默认true
        binding.simpleSwipeLayout.leftMenuEnable = true
        //右菜单使能 默认true
        binding.simpleSwipeLayout.rightMenuEnable = true
        //清除菜单
        //binding.simpleSwipeLayout.leftMenuView.clearMenu()
    }

}