package com.benjaminwan.epoxyswipedemo.ui.fragment

import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.View
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.airbnb.mvrx.withState
import com.benjaminwan.epoxyswipedemo.R
import com.benjaminwan.epoxyswipedemo.app.App
import com.benjaminwan.epoxyswipedemo.databinding.FragmentRecyclerviewBinding
import com.benjaminwan.epoxyswipedemo.menu.leftMenus
import com.benjaminwan.epoxyswipedemo.menu.rightMenus
import com.benjaminwan.epoxyswipedemo.ui.adapter.TestAdapter
import com.benjaminwan.epoxyswipedemo.ui.adapter.TestItemClickListener
import com.benjaminwan.epoxyswipedemo.ui.callback.MenuItemDragListener
import com.benjaminwan.epoxyswipedemo.ui.callback.MenuItemHelperCallBack
import com.benjaminwan.epoxyswipedemo.utils.setItemDecoration
import com.benjaminwan.epoxyswipedemo.utils.shakeInfinite
import com.benjaminwan.epoxyswipedemo.utils.showToast
import com.benjaminwan.epoxyswipedemo.utils.viewBinding
import com.benjaminwan.swipemenulayout.SwipeMenuItem
import com.benjaminwan.swipemenulayout.SwipeMenuLayout
import com.benjaminwan.swipemenulayout.helper.MenuItemTouchHelper
import com.orhanobut.logger.Logger

class RecyclerViewAdapterFragment(@LayoutRes contentLayoutId: Int = R.layout.fragment_recyclerview) :
    Fragment(contentLayoutId) {

    private val binding: FragmentRecyclerviewBinding by viewBinding()
    private lateinit var adapter: TestAdapter
    private val demos = (0..99).map { "Test Drag & SwipeMenu Item $it" }
    private val itemTouchHelper = MenuItemTouchHelper(
        MenuItemHelperCallBack(
            object :
                MenuItemDragListener {
                var objectAnimator: ObjectAnimator? = null
                override fun onItemMoved(fromPosition: Int, toPosition: Int) {
                    adapter.move(fromPosition,toPosition)
                }

                override fun onDragStarted(itemView: View?, adapterPosition: Int) {
                    Logger.i("onDragStarted")
                    itemView ?: return
                    objectAnimator = itemView.shakeInfinite()
                }

                override fun onDragReleased() {
                    Logger.i("onDragReleased")
                    objectAnimator?.cancel()
                }

            })
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapter = TestAdapter(requireContext())
        adapter.setDemos(demos)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private fun initViews() {
        binding.demoRv.setHasFixedSize(true)
        binding.demoRv.layoutManager = LinearLayoutManager(requireContext())
        binding.demoRv.setItemDecoration(4, 4, 4, 4)
        binding.demoRv.adapter = adapter
        itemTouchHelper.attachToRecyclerView(binding.demoRv)
        adapter.listener = object : TestItemClickListener {
            override fun onItemClick(
                swipeMenuLayout: SwipeMenuLayout,
                view: View,
                itemContent: String
            ) {
                App.instance.showToast("Click: $itemContent")
            }

            override fun onMenuItemClick(
                swipeMenuLayout: SwipeMenuLayout,
                itemContent: String,
                swipeMenuItem: SwipeMenuItem
            ) {
                App.instance.showToast("Click: ${swipeMenuItem.title}")
                onMenuItemClick(swipeMenuItem, itemContent)
                swipeMenuLayout.closeMenu()

            }
        }
    }

    private fun onMenuItemClick(item: SwipeMenuItem, content: String) {
        when (item.id) {
            leftMenus[0].id -> {
                adapter.remove(content)
            }
            leftMenus[1].id -> {
                adapter.clear()
            }
            rightMenus[0].id -> {
                adapter.remove(content)
            }
            rightMenus[1].id -> {
                adapter.setDemos(demos)
            }
            else -> {
            }
        }
    }

}