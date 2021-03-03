# SwipeMenuLayout

### 介绍
SwipeMenuLayout是一个侧滑菜单控件，支持向左滑动(右侧菜单)或向右滑动(左侧菜单)，并可用于RecyclerView。
开发这个控件主要是为了配合epoxy实现侧滑菜单功能。

### 相关库
[Mavericks](https://github.com/airbnb/mavericks)

[epoxy](https://github.com/airbnb/epoxy)

### 安装
```groovy
dependencies {
  implementation project(':SwipeMenuLayout')
}
```

### 使用说明

#### 在Layout中定义
```xml
<com.benjaminwan.swipemenulayout.SwipeMenuLayout
    android:id="@+id/swipeLayout"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    app:contentView="@id/contentLayout">
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
                width = 60
                height = ViewGroup.LayoutParams.MATCH_PARENT
                title = "Left Delete"
                titleColorRes = R.color.selector_white_to_grey5
                backgroundRes = R.drawable.bg_orange5_to_orange7
                iconRes = R.drawable.ic_delete
                iconColorRes = R.color.selector_white_to_grey5
            }
            menuItem {
                width = 80
                height = ViewGroup.LayoutParams.MATCH_PARENT
                title = "Left Clear"
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
                .setWidth(60)
                .setHeight(ViewGroup.LayoutParams.MATCH_PARENT)
                .setTitle("Right Delete")
                .setTitleColor(R.color.selector_white_to_grey5)
                .setBackground(R.drawable.bg_orange5_to_orange7)
                .setIcon(R.drawable.ic_delete)
                .setIconColor(R.color.selector_white_to_grey5)
                .build(),
            SwipeMenuItem.Builder()
                .setWidth(80)
                .setHeight(ViewGroup.LayoutParams.MATCH_PARENT)
                .setTitle("Right Clear")
                .setTitleColor(R.color.selector_white_to_grey5)
                .setBackground(R.drawable.bg_red5_to_red7)
                .setIcon(R.drawable.ic_clear)
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