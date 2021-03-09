package com.benjaminwan.epoxyswipedemo.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.benjaminwan.epoxyswipedemo.R
import com.benjaminwan.epoxyswipedemo.menu.leftMenus
import com.benjaminwan.epoxyswipedemo.menu.rightMenus
import com.benjaminwan.swipemenulayout.SwipeMenuLayout

class TestAdapter(private val context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var listener: TestItemClickListener? = null

    private val demos: MutableList<String> = mutableListOf()
    fun setDemos(data: List<String>) {
        demos.clear()
        demos.addAll(data)
        notifyDataSetChanged()
    }

    fun remove(content: String) {
        if (demos.contains(content)) {
            val index = demos.indexOf(content)
            demos.removeAt(index)
            notifyItemRemoved(index)
        }
    }

    fun clear() {
        demos.clear()
        notifyDataSetChanged()
    }

    fun move(from: Int, to: Int) {
        val removed = demos.removeAt(from)
        val toPost = if (to > from) {
            to - 1
        } else {
            to
        }
        demos.add(toPost, removed)
        notifyItemMoved(from, to)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.rv_menu_demo_item, parent, false)
        return TestMenuItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is TestMenuItemViewHolder) {
            holder.contentLayout.setOnClickListener { view ->
                listener?.onItemClick(holder.swipeMenuLayout, view, demos[holder.adapterPosition])
            }
            holder.contentTv.text = demos[position]
            holder.swipeMenuLayout.leftMenuView.setOnMenuItemClickListener { item ->
                listener?.onMenuItemClick(holder.swipeMenuLayout, demos[holder.adapterPosition], item)
            }
            holder.swipeMenuLayout.rightMenuView.setOnMenuItemClickListener { item ->
                listener?.onMenuItemClick(holder.swipeMenuLayout, demos[holder.adapterPosition], item)
            }
            holder.swipeMenuLayout.leftMenuView.createMenu(leftMenus)
            holder.swipeMenuLayout.rightMenuView.createMenu(rightMenus)
            holder.swipeMenuLayout.addOnMenuLeftOpenedListener {
                holder.leftIv.setImageResource(R.drawable.ic_left)
            }
            holder.swipeMenuLayout.addOnMenuRightOpenedListener {
                holder.rightIv.setImageResource(R.drawable.ic_right)
            }
            holder.swipeMenuLayout.addOnMenuClosedListener {
                holder.leftIv.setImageResource(R.drawable.ic_right)
                holder.rightIv.setImageResource(R.drawable.ic_left)
            }
        }
    }

    override fun getItemCount(): Int = demos.size

    inner class TestMenuItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val contentLayout: LinearLayout = itemView.findViewById(R.id.contentLayout)
        val contentTv: TextView = itemView.findViewById(R.id.demoTv)
        val swipeMenuLayout: SwipeMenuLayout = itemView.findViewById(R.id.swipeMenuLayout)
        val leftIv: ImageView = itemView.findViewById(R.id.leftIv)
        val rightIv: ImageView = itemView.findViewById(R.id.rightIv)
    }

}