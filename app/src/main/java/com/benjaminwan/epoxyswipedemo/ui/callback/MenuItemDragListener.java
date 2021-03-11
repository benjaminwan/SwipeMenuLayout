package com.benjaminwan.epoxyswipedemo.ui.callback;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

public interface MenuItemDragListener {
    void onItemMoved(int fromPosition, int toPosition);

    void onDragStarted(View itemView, int adapterPosition);

    void onDragReleased();

    void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder);
}
