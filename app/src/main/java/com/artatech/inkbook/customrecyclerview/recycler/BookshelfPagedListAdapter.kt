package com.artatech.inkbook.customrecyclerview.recycler

import android.content.Context
import android.view.ContextMenu
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

abstract class BookshelfPagedListAdapter<T>(val context: Context, callback : DiffCallback<T>) : PagedListAdapter<T, BookshelfPagedListAdapter.ViewHolder<T>>(callback) {

    private var mOnClickListener : ((T, Int) -> Unit)? = null
    private var mView: RecyclerView? = null

    abstract class DiffCallback<T> : DiffUtil.ItemCallback<T>() {

    }

    override fun onBindViewHolder(holder: ViewHolder<T>, position: Int) {
        val item = getItem(position)
        holder.itemView.setOnClickListener { mOnClickListener?.invoke(item!!, position) }
        holder.onBind(item)
    }

    open fun setOnItemClickListener(listener : ((T, Int) -> Unit)?) {
        mOnClickListener = listener
    }

    override fun getItemViewType(position: Int): Int {
        return context.getSharedPreferences(BookshelfPreferences.NAME, Context.MODE_PRIVATE).getInt(BookshelfPreferences.KEY_VIEW_TYPE, ViewType.LIST)
    }


    abstract class ViewHolder<T>(val context : Context, itemView : View) : RecyclerView.ViewHolder(itemView) {
        abstract fun onBind(item : T?)
    }

    class ContextMenuInfo<T>(val targetView: View, val item: T, val position: Int) : ContextMenu.ContextMenuInfo


    class ViewType {
        companion object {
            val GRID = 1
            val LIST = 0
        }
    }
}