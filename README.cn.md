# SwipeMenuLayout
[![Build Status](https://jitpack.io/v/benjaminwan/SwipeMenuLayout.svg)](https://jitpack.io/v/benjaminwan/SwipeMenuLayout.svg)
[![Issue](https://img.shields.io/github/issues/benjaminwan/SwipeMenuLayout.svg)](https://github.com/benjaminwan/SwipeMenuLayout/issues)
[![Star](https://img.shields.io/github/stars/benjaminwan/SwipeMenuLayout.svg)](https://github.com/benjaminwan/SwipeMenuLayout)

## [英文](./README.md) | [中文](./README.cn.md)

### 介绍
SwipeMenuLayout是一个Android侧滑菜单控件。

支持向左滑动(右侧菜单)或向右滑动(左侧菜单)，并可用于RecyclerView，支持长按拖拽排序。

demo实现：Kotlin + [epoxy](https://github.com/airbnb/epoxy) + [Mavericks](https://github.com/airbnb/mavericks)

#### 演示效果图
##### 单独使用
![avatar](demo_pictures/simple.gif)

##### RecyclerView+Adapter
![avatar](demo_pictures/recyclerview.gif)

##### RecyclerView+epoxy+types
![avatar](demo_pictures/recyclerview_types.gif)

### 安装
0. root build.gradle添加
```groovy
repositories {
        google()
        jcenter()
        maven { url "https://jitpack.io" }
    }
```
1. 单独使用控件，仅需要Core
2. 在RecyclerView+Adapter中使用时，需要Core和Helper
3. 在RecyclerView+Epoxy中使用时，需要全部3个
```groovy
dependencies {
    def swipe_version = "1.1.1"
    //Widget
    implementation "com.github.benjaminwan.SwipeMenuLayout:SwipeMenuLayout-Core:$swipe_version"
    //Adapter Helper
    implementation "com.github.benjaminwan.SwipeMenuLayout:SwipeMenuLayout-Helper:$swipe_version"
    //Epoxy Helper
    implementation "com.github.benjaminwan.SwipeMenuLayout:SwipeMenuLayout-EpoxyHelper:$swipe_version"
}
```

### 使用说明

#### 在Layout中定义
SwipeMenuLayout下的第一个View默认成为contentView。
也可以通过app:contentView="@id/contentLayout"指定contentView
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
            android:text="Simple Usage"
            android:textColor="@color/white" />
</com.benjaminwan.swipemenulayout.SwipeMenuLayout>
```

#### 定义菜单
两种方法二选一
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
菜单View是SwipeMenuLayout的成员：leftMenuView、rightMenuView
```kotlin
swipeLayout.leftMenuView.createMenu(leftMenus)
swipeLayout.rightMenuView.createMenu(rightMenus)
```

#### 清除菜单
```kotlin
swipeLayout.leftMenuView.removeAllViews()
swipeLayout.rightMenuView.removeAllViews()
```

#### 左右滑动使能
```kotlin
swipeLayout.leftMenuEnable = true
swipeLayout.rightMenuEnable = true
```

#### 菜单打开关闭事件
```kotlin
swipeLayout.addOnMenuClosedListener {
    //...
}
swipeLayout.addOnLeftMenuOpenListener {
    //...
}
swipeLayout.addOnRightMenuOpenListener {
    //...
}
```

#### 菜单项点击事件
```kotlin
swipeLayout.leftMenuView.setOnMenuItemClickListener {
    //...
}
swipeLayout.rightMenuView.setOnMenuItemClickListener {
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

#### 其它
MenuItemTouchHelper直接复制粘贴了ItemTouchHelper的代码。

在RecyclerView中使用拖拽排序时，拖拽第一个item向下移动会导致快速向下滚动的问题。

导致难以让第一个item移到第二的位置。

所以建议在第一个item的位置以一个不可拖拽的view来代替。

