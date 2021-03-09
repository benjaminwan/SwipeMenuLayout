package com.benjaminwan.epoxyswipedemo.ui.fragment

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
import com.benjaminwan.epoxyswipedemo.itemviews.MenuDemoItemViewModel_
import com.benjaminwan.epoxyswipedemo.itemviews.menuDemoItemView
import com.benjaminwan.epoxyswipedemo.menu.leftMenus
import com.benjaminwan.epoxyswipedemo.menu.rightMenus
import com.benjaminwan.epoxyswipedemo.ui.callback.MenuItemDragListener
import com.benjaminwan.epoxyswipedemo.ui.callback.MenuItemHelperCallBack
import com.benjaminwan.epoxyswipedemo.ui.viewmodel.RecyclerViewEpoxyViewModel
import com.benjaminwan.epoxyswipedemo.utils.*
import com.benjaminwan.swipemenulayout.SwipeMenuItem
import com.benjaminwan.swipemenulayout.epoxyhelper.EpoxyMenuTouchHelper
import com.benjaminwan.swipemenulayout.helper.MenuItemTouchHelper
import com.orhanobut.logger.Logger

class RecyclerViewEpoxyFragment(@LayoutRes contentLayoutId: Int = R.layout.fragment_recyclerview) :
    Fragment(contentLayoutId), MavericksView {

    private val binding: FragmentRecyclerviewBinding by viewBinding()
    private val demoVM by activityViewModel(RecyclerViewEpoxyViewModel::class)
    private val epoxyController by lazy { epoxyController() }

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
        EpoxyMenuTouchHelper.initDragging(epoxyController)
            .withRecyclerView(binding.demoRv)
            .forVerticalList()
            .withTarget(MenuDemoItemViewModel_::class.java)
            .andCallbacks(object : EpoxyMenuTouchHelper.DragCallbacks<MenuDemoItemViewModel_>() {
                var objectAnimator: ObjectAnimator? = null

                override fun onModelMoved(
                    fromPosition: Int,
                    toPosition: Int,
                    modelBeingMoved: MenuDemoItemViewModel_?,
                    itemView: View?
                ) {
                    withState(demoVM) {
                        demoVM.swap(fromPosition, toPosition)
                    }
                }

                override fun onDragStarted(
                    model: MenuDemoItemViewModel_?,
                    itemView: View?,
                    adapterPosition: Int
                ) {
                    super.onDragStarted(model, itemView, adapterPosition)
                    itemView ?: return
                    objectAnimator = itemView.shakeInfinite()
                }

                override fun onDragReleased(model: MenuDemoItemViewModel_?, itemView: View?) {
                    super.onDragReleased(model, itemView)
                    objectAnimator?.cancel()
                }
            })
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