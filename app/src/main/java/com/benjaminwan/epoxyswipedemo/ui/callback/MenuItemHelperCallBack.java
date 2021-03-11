package com.benjaminwan.epoxyswipedemo.ui.callback;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.benjaminwan.epoxyswipedemo.ui.adapter.TestAdapter;
import com.benjaminwan.swipemenulayout.helper.MenuItemTouchHelper;

public class MenuItemHelperCallBack extends MenuItemTouchHelper.Callback {

    private MenuItemDragListener menuItemDragListener;

    public MenuItemHelperCallBack(MenuItemDragListener menuItemDragListener) {
        this.menuItemDragListener = menuItemDragListener;
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return true;
    }

    /**
     * 返回可以滑动的方向
     *
     * @param recyclerView
     * @param viewHolder
     * @return
     */
    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        if (viewHolder == null) {
            return makeMovementFlags(0, 0);
        }
        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        int dragFlags;
        if (manager instanceof GridLayoutManager || manager instanceof StaggeredGridLayoutManager) {
            dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
        } else {
            dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
        }
        if (viewHolder.getItemViewType() == TestAdapter.TYPE_HEADER) {
            dragFlags = 0;
        }
        return makeMovementFlags(dragFlags, 0);
    }

    @Override
    public void onSelectedChanged(@Nullable RecyclerView.ViewHolder viewHolder, int actionState) {
        super.onSelectedChanged(viewHolder, actionState);

        if (actionState == ItemTouchHelper.ACTION_STATE_DRAG && viewHolder != null) {
            View itemView = viewHolder.itemView;
            if (menuItemDragListener != null) {
                menuItemDragListener.onDragStarted(itemView, viewHolder.getAdapterPosition());
            }
        } else if (actionState == ItemTouchHelper.ACTION_STATE_IDLE && viewHolder == null) {
            if (menuItemDragListener != null) {
                menuItemDragListener.onDragReleased();
            }
        }
    }

    /**
     * 拖拽到新位置时候的回调方法
     *
     * @param recyclerView
     * @param viewHolder
     * @param target
     * @return
     */
    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        //不同Type之间不允许移动
        if (viewHolder.getItemViewType() != target.getItemViewType()) {
            return false;
        }
        if (menuItemDragListener != null) {
            menuItemDragListener.onItemMoved(viewHolder.getAdapterPosition(), target.getAdapterPosition());
        }
        return true;
    }

    /**
     * 未使用
     *
     * @param viewHolder
     * @param direction
     * @return
     */
    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
    }

    @Override
    public void clearView(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        if (menuItemDragListener != null) {
            menuItemDragListener.clearView(recyclerView, viewHolder);
        }
        super.clearView(recyclerView, viewHolder);
    }

    public void setOnItemDragListeber(MenuItemDragListener menuItemDragListener) {
        this.menuItemDragListener = menuItemDragListener;
    }
}
