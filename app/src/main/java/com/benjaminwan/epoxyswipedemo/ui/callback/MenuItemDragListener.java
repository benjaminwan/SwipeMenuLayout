package com.benjaminwan.epoxyswipedemo.ui.callback;

import android.view.View;

public interface MenuItemDragListener {
    void onItemMoved(int fromPosition, int toPosition);

    void onDragStarted(View itemView, int adapterPosition);

    void onDragReleased();
}
