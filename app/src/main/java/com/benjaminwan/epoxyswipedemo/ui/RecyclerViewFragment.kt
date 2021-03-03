package com.benjaminwan.epoxyswipedemo.ui

import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.View
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import com.airbnb.mvrx.MavericksView
import com.airbnb.mvrx.activityViewModel
import com.airbnb.mvrx.withState
import com.benjaminwan.epoxyswipedemo.R
import com.benjaminwan.epoxyswipedemo.databinding.FragmentRecyclerviewBinding
import com.benjaminwan.epoxyswipedemo.itemviews.menuDemoItemView
import com.benjaminwan.epoxyswipedemo.menu.leftMenus
import com.benjaminwan.epoxyswipedemo.menu.rightMenus
import com.benjaminwan.epoxyswipedemo.ui.callback.MenuItemDragListener
import com.benjaminwan.epoxyswipedemo.ui.callback.MenuItemHelperCallBack
import com.benjaminwan.epoxyswipedemo.utils.*
import com.benjaminwan.swipemenulayout.SwipeMenuItem
import com.benjaminwan.swipemenulayout.helper.MenuItemTouchHelper
import com.orhanobut.logger.Logger

class RecyclerViewFragment(@LayoutRes contentLayoutId: Int = R.layout.fragment_recyclerview) :
    Fragment(contentLayoutId), MavericksView {

    private val binding: FragmentRecyclerviewBinding by viewBinding()
    private val demoVM by activityViewModel(RecyclerViewViewModel::class)
    private val epoxyController by lazy { epoxyController() }

    private val itemTouchHelper = MenuItemTouchHelper(
        MenuItemHelperCallBack(
            object :
                MenuItemDragListener {
                var objectAnimator: ObjectAnimator? = null
                override fun onItemMoved(fromPosition: Int, toPosition: Int) {
                    withState(demoVM) {
                        if (fromPosition in it.demos.indices && toPosition in it.demos.indices)
                            demoVM.swap(fromPosition, toPosition)
                    }
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
        epoxyController.onRestoreInstanceState(savedInstanceState)
        demoVM.initDemos()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        epoxyController.onSaveInstanceState(outState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private fun initViews() {
        binding.demoRv.setHasFixedSize(true)
        binding.demoRv.setItemDecoration(4, 4, 4, 4)
        binding.demoRv.setController(epoxyController)
        //拖拽事件(Drag and drop event)
        itemTouchHelper.attachToRecyclerView(binding.demoRv)
    }

    override fun invalidate() = withState(demoVM) { state ->
        binding.demoRv.requestModelBuild()
    }

    private fun epoxyController() = simpleController(demoVM) { state ->
        state.demos.forEachIndexed { index, demo ->
            menuDemoItemView {
                id("demo_${demo}}")
                demoContent(demo)
                onClickListener { swipeMenuLayout, view ->
                    showToast("Click: $demo")
                }
                leftMenu(leftMenus)
                onLeftMenuClickListener { swipeMenuLayout, swipeMenuItem ->
                    showToast("Left Click: $demo ${swipeMenuItem.title}")
                    swipeMenuLayout.closeMenu()
                    onMenuItemClick(swipeMenuItem, demo)
                }
                onLeftMenuOpenedListener {
                    //showToast("Left Menu Opened:$demo")
                }
                rightMenu(rightMenus)
                onRightMenuClickListener { swipeMenuLayout, swipeMenuItem ->
                    showToast("Right Click: $demo ${swipeMenuItem.title}")
                    swipeMenuLayout.closeMenu()
                    onMenuItemClick(swipeMenuItem, demo)
                }
                onRightMenuOpenedListener {
                    //showToast("Right Opened:$demo")
                }
                onMenuClosedListener {
                    //showToast("Menu Closed: $demo")
                }
            }
        }
    }

    private fun onMenuItemClick(item: SwipeMenuItem, content: String) {
        when (item.id) {
            leftMenus[0].id -> {
                demoVM.remove(content)
            }
            leftMenus[1].id -> {
                demoVM.clear()
            }
            rightMenus[0].id -> {
                demoVM.remove(content)
            }
            rightMenus[1].id -> {
                demoVM.initDemos()
            }
            else -> {
            }
        }
    }

}