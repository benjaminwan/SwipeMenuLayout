# SwipeMenuLayout

### 介绍
SwipeMenuLayout是一个侧滑菜单控件，支持向左滑动(右侧菜单)或向右滑动(左侧菜单)，并可用于RecyclerView。
开发这个控件主要是为了配合epoxy实现侧滑菜单功能。
支持RecyclerView长按拖拽排序。

### 相关库
[Mavericks](https://github.com/airbnb/mavericks)

[epoxy](https://github.com/airbnb/epoxy)

### 安装
1. 单独使用控件，仅需要SwipeMenuLayout
2. 控件在RecyclerView+Adapter中使用时，需要SwipeMenuLayout、SwipeMenuLayout-Helper
3. 控件在RecyclerView+Epoxy中使用时，需要SwipeMenuLayout、SwipeMenuLayout-Helper、SwipeMenuLayout-EpoxyHelper
```groovy
dependencies {
    //Widget
    implementation project(':SwipeMenuLayout')
    //Adapter Helper
    implementation project(':SwipeMenuLayout-Helper')
    //Epoxy Helper
    implementation project(':SwipeMenuLayout-EpoxyHelper')
}
```

### 使用说明

#### 在Layout中定义
```xml
<com.benjaminwan.swipemenulayout.SwipeMenuLayout
    android:id="@+id/swipeLayout"
    android:layout_width="match_parent"
    android:layout_height="wrap_content">
    <TextView
            android:id="@+id/contentLayout"
            android:layout_width="0dp"
            android:layout_height="match_parent"
            android:layout_weight="1"
            android:gravity="center"
            android:text="Normal Usage"
            android:textColor="@color/white" />
</com.benjaminwan.swipemenulayout.SwipeMenuLayout>
```

#### 定义菜单
```kotlin
//1. 使用DSL
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
//2. 或者Builder
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
```
#### 添加菜单
```kotlin
simpleSwipeLayout.leftMenuView.createMenu(leftMenus)
simpleSwipeLayout.rightMenuView.createMenu(rightMenus)
```

#### 清除菜单
```kotlin
simpleSwipeLayout.leftMenuView.clearMenu()
simpleSwipeLayout.rightMenuView.clearMenu()
```

#### 菜单使能
```kotlin
simpleSwipeLayout.leftMenuEnable = true
simpleSwipeLayout.rightMenuEnable = true
```

#### 菜单打开关闭事件
```kotlin
simpleSwipeLayout.addOnMenuClosedListener {
    //...
}
simpleSwipeLayout.addOnLeftMenuOpenListener {
    //...
}
simpleSwipeLayout.addOnRightMenuOpenListener {
    //...
}
```

#### 菜单项点击事件
```kotlin
simpleSwipeLayout.leftMenuView.setOnMenuItemClickListener {
    //...
}
simpleSwipeLayout.rightMenuView.setOnMenuItemClickListener {
    //...
}
```

#### RecyclerView+Adapter拖拽排序
```kotlin
val itemTouchHelper = MenuItemTouchHelper(
        MenuItemHelperCallBack(
            object :
                MenuItemDragListener {
                var objectAnimator: ObjectAnimator? = null
                override fun onItemMoved(fromPosition: Int, toPosition: Int) {
                    adapter.move(fromPosition,toPosition)
                }

                override fun onDragStarted(itemView: View?, adapterPosition: Int) {
                    itemView ?: return
                    objectAnimator = itemView.shakeInfinite()
                }

                override fun onDragReleased() {
                    objectAnimator?.cancel()
                }

            })
    )
itemTouchHelper.attachToRecyclerView(recyclerView)
```
#### RecyclerView+Epoxy拖拽排序
```kotlin
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
```

#### 效果图
