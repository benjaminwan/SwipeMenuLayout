package com.benjaminwan.swipemenulayout

import android.content.Context
import android.graphics.PointF
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.Scroller
import com.benjaminwan.swipemenulayout.MenuState.*
import java.lang.ref.WeakReference
import java.util.*
import kotlin.math.abs
import kotlin.math.max

open class SwipeMenuLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : ViewGroup(context, attrs, defStyleAttr, defStyleRes) {
    private val marginParams =
        MarginLayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT)
    var leftMenuView: SwipeMenuView = SwipeMenuView(context, attrs, defStyleAttr).apply {
        isClickable = true
        layoutParams = marginParams
    }
    var rightMenuView: SwipeMenuView = SwipeMenuView(context, attrs, defStyleAttr).apply {
        isClickable = true
        layoutParams = marginParams
    }
    private var scaledTouchSlop: Int = ViewConfiguration.get(context).scaledTouchSlop
    private var scroller: Scroller = Scroller(context)

    private val matchParentChildren = ArrayList<View>(1)
    private var contentViewResID: Int = -1
    private var contentView: View? = null

    private var isSwiping: Boolean = false
    private var lastP: PointF = PointF()
    private var firstP: PointF = PointF()
    var menuOpenThreshold = 0.3f
    var rightMenuEnable = true
    var leftMenuEnable = true

    private var finalDistanceX: Float = 0.toFloat()
    private var menuState: MenuState = CLOSE

    init {
        val typedArray = context.theme.obtainStyledAttributes(
            attrs, R.styleable.SwipeMenuLayout,
            defStyleAttr, 0
        )
        try {
            for (i in 0 until typedArray.indexCount) {
                when (typedArray.getIndex(i)) {
                    R.styleable.SwipeMenuLayout_leftMenuEnable -> rightMenuEnable =
                        typedArray.getBoolean(R.styleable.SwipeMenuLayout_leftMenuEnable, true)
                    R.styleable.SwipeMenuLayout_rightMenuEnable -> leftMenuEnable =
                        typedArray.getBoolean(R.styleable.SwipeMenuLayout_rightMenuEnable, true)
                    R.styleable.SwipeMenuLayout_menuOpenThreshold -> menuOpenThreshold =
                        typedArray.getFloat(R.styleable.SwipeMenuLayout_menuOpenThreshold, 0.3f)
                    R.styleable.SwipeMenuLayout_contentView -> contentViewResID =
                        typedArray.getResourceId(R.styleable.SwipeMenuLayout_contentView, -1)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            typedArray.recycle()
        }

        this.addView(leftMenuView)
        this.addView(rightMenuView)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        //获取childView的个数
        isClickable = true
        var count = childCount
        //参考frameLayout测量代码
        val measureMatchParentChildren =
            MeasureSpec.getMode(widthMeasureSpec) != MeasureSpec.EXACTLY || MeasureSpec.getMode(
                heightMeasureSpec
            ) != MeasureSpec.EXACTLY
        matchParentChildren.clear()
        var maxHeight = 0
        var maxWidth = 0
        var childState = 0
        //childViews
        for (i in 0 until count) {
            val child = getChildAt(i)
            if (child.visibility != View.GONE) {
                measureChildWithMargins(child, widthMeasureSpec, 0, heightMeasureSpec, 0)
                val lp = child.layoutParams as MarginLayoutParams
                maxWidth =
                    maxWidth.coerceAtLeast(child.measuredWidth + lp.leftMargin + lp.rightMargin)
                maxHeight =
                    maxHeight.coerceAtLeast(child.measuredHeight + lp.topMargin + lp.bottomMargin)
                childState = View.combineMeasuredStates(childState, child.measuredState)
                if (measureMatchParentChildren) {
                    if (lp.width == LayoutParams.MATCH_PARENT || lp.height == LayoutParams.MATCH_PARENT) {
                        matchParentChildren.add(child)
                    }
                }
            }
        }
        // Check against our minimum height and width
        maxHeight = maxHeight.coerceAtLeast(suggestedMinimumHeight)
        maxWidth = maxWidth.coerceAtLeast(suggestedMinimumWidth)
        setMeasuredDimension(
            View.resolveSizeAndState(maxWidth, widthMeasureSpec, childState),
            View.resolveSizeAndState(
                maxHeight, heightMeasureSpec,
                childState shl View.MEASURED_HEIGHT_STATE_SHIFT
            )
        )

        count = matchParentChildren.size
        if (count > 1) {
            for (i in 0 until count) {
                val child = matchParentChildren[i]
                val lp = child.layoutParams as MarginLayoutParams
                val childWidthMeasureSpec: Int = if (lp.width == LayoutParams.MATCH_PARENT) {
                    val width = max(
                        0, measuredWidth
                                - lp.leftMargin - lp.rightMargin
                    )
                    MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY)
                } else {
                    getChildMeasureSpec(
                        widthMeasureSpec,
                        lp.leftMargin + lp.rightMargin,
                        lp.width
                    )
                }
                val childHeightMeasureSpec: Int =
                    if (lp.height == FrameLayout.LayoutParams.MATCH_PARENT) {
                        val height = max(
                            0, measuredHeight
                                    - lp.topMargin - lp.bottomMargin
                        )
                        MeasureSpec.makeMeasureSpec(
                            height, MeasureSpec.EXACTLY
                        )
                    } else {
                        getChildMeasureSpec(
                            heightMeasureSpec,
                            lp.topMargin + lp.bottomMargin,
                            lp.height
                        )
                    }

                child.measure(childWidthMeasureSpec, childHeightMeasureSpec)
            }
        }

    }

    override fun generateLayoutParams(attrs: AttributeSet): LayoutParams {
        return MarginLayoutParams(context, attrs)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        val count = childCount
        val left = 0 + paddingLeft
        val right = 0 + paddingLeft
        val top = 0 + paddingTop
        val bottom = 0 + paddingTop

        if (contentViewResID != -1) {
            for (i in 0 until count) {
                val child = getChildAt(i)
                if (contentView == null && child.id == contentViewResID) {
                    contentView = child
                    contentView?.isClickable = true
                }
            }
        } else {
            for (i in 0 until count) {
                val child = getChildAt(i)
                if (contentView == null && child !is SwipeMenuView) {
                    contentView = child
                    contentView?.isClickable = true
                }
            }
        }
        //布局contentView
        var cRight = 0
        val contentView = contentView ?: return
        val contentViewLp = layoutParams as MarginLayoutParams
        with(contentView) {
            val cTop = top + contentViewLp.topMargin
            val cLeft = left + contentViewLp.leftMargin
            cRight = left + contentViewLp.leftMargin + measuredWidth
            val cBottom = cTop + measuredHeight
            layout(cLeft, cTop, cRight, cBottom)
        }
        with(leftMenuView) {
            val leftViewLp = layoutParams as MarginLayoutParams
            val lTop = top + leftViewLp.topMargin
            val lLeft =
                0 - measuredWidth + leftViewLp.leftMargin + leftViewLp.rightMargin
            val lRight = 0 - leftViewLp.rightMargin
            val lBottom = lTop + measuredHeight
            layout(lLeft, lTop, lRight, lBottom)
        }
        with(rightMenuView) {
            val rightViewLp = layoutParams as MarginLayoutParams
            val lTop = top + rightViewLp.topMargin
            val lLeft = contentView.right + contentViewLp.rightMargin + rightViewLp.leftMargin
            val lRight = lLeft + measuredWidth
            val lBottom = lTop + measuredHeight
            layout(lLeft, lTop, lRight, lBottom)
        }
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        val contentView = contentView ?: return super.dispatchTouchEvent(ev)
        when (ev.action) {
            MotionEvent.ACTION_DOWN -> {
                isSwiping = false
                lastP.set(ev.rawX, ev.rawY)
                firstP.set(ev.rawX, ev.rawY)
                if (lastInstance != null) {
                    if (lastInstance?.get() !== this) {
                        lastInstance?.get()?.close()
                    }
                    // Log.i(TAG, ">>>有菜单被打开");
                    parent.requestDisallowInterceptTouchEvent(true)
                }
            }
            MotionEvent.ACTION_MOVE -> {
                val distanceX = lastP.x - ev.rawX
                val distanceY = lastP.y - ev.rawY
                if (abs(distanceY) > scaledTouchSlop && abs(distanceY) > abs(distanceX)) {

                } else {
                    scrollBy(distanceX.toInt(), 0)//滑动使用scrollBy
                    //越界修正
                    if (scrollX < 0) {
                        if (!leftMenuEnable || leftMenuView.childCount == 0) {
                            scrollTo(0, 0)
                        } else {//左滑
                            if (scrollX < leftMenuView.left) {
                                scrollTo(leftMenuView.left, 0)
                            }
                        }
                    } else if (scrollX > 0) {
                        if (!rightMenuEnable || rightMenuView.childCount == 0) {
                            scrollTo(0, 0)
                        } else {
                            val contentViewLp = contentView.layoutParams as MarginLayoutParams
                            if (scrollX > rightMenuView.right - contentView.right - contentViewLp.rightMargin) {
                                scrollTo(
                                    rightMenuView.right - contentView.right - contentViewLp.rightMargin,
                                    0
                                )
                            }
                        }
                    }
                    //当处于水平滑动时，禁止父类拦截
                    if (abs(distanceX) > scaledTouchSlop) {
                        parent.requestDisallowInterceptTouchEvent(true)
                    }
                    lastP.set(ev.rawX, ev.rawY)
                }
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                /*val width = mLeftView?.width ?: 0
                if (ev.x > width) {
                    handlerSwipeMenu(CLOSE)
                }*/
                /*val width = mRightView?.width ?:0
                if (ev.x > width) {
                    handlerSwipeMenu(CLOSE)
                }*/
                finalDistanceX = firstP.x - ev.rawX
                if (abs(finalDistanceX) > scaledTouchSlop) {
                    isSwiping = true
                }
                val state = isShouldOpen(scrollX)
                handlerSwipeMenu(state, state == menuState)
            }
            else -> {
            }
        }

        return super.dispatchTouchEvent(ev)
    }

    private fun isClickOnContentViewLeft(x: Float): Boolean {
        return x > leftMenuView.width
    }

    private fun isClickOnContentViewRight(x: Float): Boolean {
        val contentView = contentView ?: return false
        return x < contentView.width - rightMenuView.width
    }

    private fun isClickOnContentView(x: Float): Boolean {
        val result = isShouldOpen(scrollX)
        if (result == LEFT_OPEN) {
            return isClickOnContentViewLeft(x)
        } else if (result == RIGHT_OPEN) {
            return isClickOnContentViewRight(x)
        }
        return false
    }

    override fun onInterceptTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {

            }
            MotionEvent.ACTION_MOVE -> {
                //滑动时拦截点击时间
                if (abs(finalDistanceX) > scaledTouchSlop) {
                    // 当手指拖动值大于mScaledTouchSlop值时，认为应该进行滚动，拦截子控件的事件
                    return true
                }
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                //滑动后不触发contentView的点击事件
                if (isSwiping) {
                    isSwiping = false
                    finalDistanceX = 0f
                    return true
                }
                val isClick = isClickOnContentView(event.x)
                if (isClick) {
                    handlerSwipeMenu(CLOSE)
                    return true
                }
            }
        }
        return super.onInterceptTouchEvent(event)
    }

    fun closeMenu() {
        handlerSwipeMenu(CLOSE)
    }

    fun openLeftMenu() {
        handlerSwipeMenu(LEFT_OPEN)
    }

    fun openRightMenu() {
        handlerSwipeMenu(RIGHT_OPEN)
    }

    private val MenuState.isClose: Boolean
        get() = this == CLOSE

    private val MenuState.isOpen: Boolean
        get() = !this.isClose

    private val MenuState.isLeftOpen: Boolean
        get() = this == LEFT_OPEN

    private val MenuState.isRightOpen: Boolean
        get() = this == RIGHT_OPEN

    val state: MenuState
        get() = menuState

    val isClosed: Boolean
        get() = menuState.isClose

    val isOpened: Boolean
        get() = menuState.isOpen

    val isLeftOpened: Boolean
        get() = menuState.isLeftOpen

    val isRightOpened: Boolean
        get() = menuState.isRightOpen

    /**
     * 设置目标状态
     *
     * @param target
     */
    private fun handlerSwipeMenu(target: MenuState, isQuiet: Boolean = false) {
        val contentView = contentView ?: return
        when (target) {
            LEFT_OPEN -> {
                scroller.startScroll(scrollX, 0, leftMenuView.left - scrollX, 0)
                lastInstance = WeakReference(this)
                if (!isQuiet) {
                    onMenuLeftOpenedListener.forEach {
                        it?.invoke(this)
                    }
                }
            }
            RIGHT_OPEN -> {
                val contentViewLp = contentView.layoutParams as MarginLayoutParams
                scroller.startScroll(
                    scrollX,
                    0,
                    rightMenuView.right - contentView.right - contentViewLp.rightMargin - scrollX,
                    0
                )
                lastInstance = WeakReference(this)
                if (!isQuiet) {
                    onMenuRightOpenedListener.forEach {
                        it?.invoke(this)
                    }
                }
            }
            else -> {
                scroller.startScroll(scrollX, 0, -scrollX, 0)
                lastInstance = null
                if (!isQuiet) {
                    onMenuClosedListener.forEach {
                        it?.invoke(this)
                    }
                }
            }
        }
        menuState = target
        invalidate()
    }

    override fun computeScroll() {
        //判断Scroller是否执行完毕：
        if (scroller.computeScrollOffset()) {
            scrollTo(scroller.currX, scroller.currY)
            //通知View重绘-invalidate()->onDraw()->computeScroll()
            invalidate()
        }
    }

    /**
     * 根据当前的scrollX的值判断松开手后应处于何种状态
     *
     * @param
     * @param scrollX
     * @return
     */
    private fun isShouldOpen(scrollX: Int): MenuState {
        if (scaledTouchSlop >= abs(finalDistanceX)) {
            return lastInstance?.get()?.state ?: CLOSE
        }
        //Log.i(TAG, ">>>finalyDistanceX:$finalDistanceX")
        if (finalDistanceX < 0) {
            //展开左边菜单
            if (scrollX < 0) {
                if (abs(leftMenuView.width * menuOpenThreshold) < abs(scrollX)) {
                    return LEFT_OPEN
                }
            }
            //关闭右边菜单
            if (scrollX > 0) {
                return CLOSE
            }
        } else if (finalDistanceX > 0) {
            //展开右边菜单
            if (scrollX > 0) {
                if (abs(rightMenuView.width * menuOpenThreshold) < abs(scrollX)) {
                    return RIGHT_OPEN
                }
            }
            //关闭左边菜单
            if (scrollX < 0) {
                return CLOSE
            }
        }
        return CLOSE
    }

    override fun onDetachedFromWindow() {
        if (this === lastInstance) {
            handlerSwipeMenu(CLOSE)
        }
        super.onDetachedFromWindow()
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        if (this === lastInstance) {
            handlerSwipeMenu(menuState)
        }
    }

    private val onMenuClosedListener: MutableList<OnMenuClosedListener> = mutableListOf()
    fun addOnMenuClosedListener(listener: OnMenuClosedListener) {
        onMenuClosedListener.add(listener)
    }

    fun removeOnMenuClosedListener(listener: OnMenuClosedListener) {
        onMenuClosedListener.remove(listener)
    }

    fun clearOnMenuClosedListener() {
        onMenuClosedListener.clear()
    }

    var onMenuLeftOpenedListener: MutableList<OnMenuLeftOpenedListener> = mutableListOf()
    fun addOnMenuLeftOpenedListener(listener: OnMenuLeftOpenedListener) {
        onMenuLeftOpenedListener.add(listener)
    }

    fun removeOnMenuLeftOpenedListener(listener: OnMenuLeftOpenedListener) {
        onMenuLeftOpenedListener.remove(listener)
    }

    fun clearOnMenuLeftOpenedListener() {
        onMenuLeftOpenedListener.clear()
    }

    var onMenuRightOpenedListener: MutableList<OnMenuRightOpenedListener> = mutableListOf()
    fun addOnMenuRightOpenedListener(listener: OnMenuRightOpenedListener) {
        onMenuRightOpenedListener.add(listener)
    }

    fun removeOnMenuRightOpenedListener(listener: OnMenuRightOpenedListener) {
        onMenuRightOpenedListener.remove(listener)
    }

    fun clearOnMenuRightOpenedListener() {
        onMenuRightOpenedListener.clear()
    }

    private fun close() {
        handlerSwipeMenu(CLOSE)
    }

    companion object {
        private val TAG = "SwipeMenuLayout"
        var lastInstance: WeakReference<SwipeMenuLayout?>? = null
    }

}
