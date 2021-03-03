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
import com.benjaminwan.epoxyswipedemo.itemviews.testItemView
import com.benjaminwan.epoxyswipedemo.menu.leftMenus
import com.benjaminwan.epoxyswipedemo.menu.rightMenus
import com.benjaminwan.epoxyswipedemo.ui.callback.MenuItemHelperCallBack
import com.benjaminwan.epoxyswipedemo.ui.callback.MenuItemDragListener
import com.benjaminwan.epoxyswipedemo.utils.*
import com.benjaminwan.swipemenulayout.SwipeMenuItem
import com.benjaminwan.swipemenulayout.helper.MenuItemTouchHelper
import com.orhanobut.logger.Logger

class RecyclerViewTypesFragment(@LayoutRes contentLayoutId: Int = R.layout.fragment_recyclerview) :
    Fragment(contentLayoutId), MavericksView {

    private val binding: FragmentRecyclerviewBinding by viewBinding()
    private val demoVM by activityViewModel(RecyclerViewViewModel::class)
    private val epoxyController by lazy { epoxyController() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        epoxyController.onRestoreInstanceState(savedInstanceState)
        val demos = (0..99).map { "Test Drag & SwipeMenu Item $it" }
        demoVM.setDemos(demos)
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
        val itemTouchHelper =
            MenuItemTouchHelper(
                MenuItemHelperCallBack(
                    object :
                        MenuItemDragListener {
                        var objectAnimator: ObjectAnimator? = null
                        override fun onItemMoved(fromPosition: Int, toPosition: Int) {
                            withState(demoVM) {
                                val fromPos = fromPosition - 6
                                val toPos = toPosition - 6
                                if (fromPos in it.demos.indices && toPos in it.demos.indices)
                                    demoVM.swap(fromPos, toPos)
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
        itemTouchHelper.attachToRecyclerView(binding.demoRv)

        /*EpoxyTouchHelper.initDragging(epoxyController)
            .withRecyclerView(demoRv)
            .forVerticalList()
            .withTarget(DemoItemViewModel_::class.java)
            .andCallbacks(object : EpoxyTouchHelper.DragCallbacks<DemoItemViewModel_>() {
                override fun onModelMoved(
                    fromPosition: Int,
                    toPosition: Int,
                    modelBeingMoved: DemoItemViewModel_?,
                    itemView: View?
                ) {
                    //因为在这个范例里，RecyclerView顶部放了6个固定的menuDemoItemView
                    //Because in this example, RecyclerView has 6 fixed menuDemoItemViews on top
                    withState(demoVM) {
                        val fromPos = fromPosition - 6
                        val toPos = toPosition - 6
                        if (fromPos in it.demos.indices && toPos in it.demos.indices)
                            demoVM.swap(fromPos, toPos)
                    }
                }*/

    }

    override fun invalidate() = withState(demoVM) { state ->
        binding.demoRv.requestModelBuild()
    }

    private fun epoxyController() = simpleController(demoVM) { state ->
        testItemView {
            id("demo_left")
            content("Left Menu")
            indexContent("0")
            onClickListener { swipeMenuLayout, view ->
                showToast("Click Item: Left Menu")
            }
            leftMenu(leftMenus)
            onLeftMenuClickListener { swipeMenuLayout, swipeMenuItem ->
                showToast("Click: ${swipeMenuItem.title}")
                swipeMenuLayout.closeMenu()
            }
        }
        testItemView {
            id("demo_open_left")
            content("Click to Open/Close Left Menu")
            indexContent("1")
            onClickListener { swipeMenuLayout, view ->
                if (swipeMenuLayout.isClosed) {
                    swipeMenuLayout.openLeftMenu()
                } else {
                    swipeMenuLayout.closeMenu()
                }
            }
            leftMenu(leftMenus)
            onLeftMenuClickListener { swipeMenuLayout, swipeMenuItem ->
                showToast("Click: ${swipeMenuItem.title}")
                swipeMenuLayout.closeMenu()
            }
            onLeftMenuOpenedListener {
                showToast("Left Menu Opened")
            }
            onMenuClosedListener {
                showToast("Menu Closed: Click to Open Left Menu")
            }
        }
        testItemView {
            id("demo_right")
            content("Right Menu")
            indexContent("2")
            onClickListener { swipeMenuLayout, view ->
                showToast("Click Item: Right Menu")
            }
            rightMenu(rightMenus)
            onRightMenuClickListener { swipeMenuLayout, swipeMenuItem ->
                showToast("Click: ${swipeMenuItem.title}")
                swipeMenuLayout.closeMenu()
            }
        }
        testItemView {
            id("demo_open_right")
            content("Click to Open/Close Right Menu")
            indexContent("3")
            onClickListener { swipeMenuLayout, view ->
                if (swipeMenuLayout.isClosed) {
                    swipeMenuLayout.openRightMenu()
                } else {
                    swipeMenuLayout.closeMenu()
                }
            }
            rightMenu(rightMenus)
            onRightMenuClickListener { swipeMenuLayout, swipeMenuItem ->
                showToast("Click: ${swipeMenuItem.title}")
                swipeMenuLayout.closeMenu()
            }
            onRightMenuOpenedListener {
                showToast("Right Menu Opened")
            }
            onMenuClosedListener {
                showToast("Menu Closed: Click to OpenRight Menu")
            }
        }
        testItemView {
            id("demo_open_Threshold8")
            content("Menu Open Threshold:0.8")
            indexContent("4")
            menuOpenThreshold(0.8f)
            leftMenu(leftMenus)
            rightMenu(rightMenus)
        }
        testItemView {
            id("demo_open_Threshold2")
            content("Menu Open Threshold:0.2")
            indexContent("5")
            menuOpenThreshold(0.2f)
            leftMenu(leftMenus)
            rightMenu(rightMenus)
        }
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