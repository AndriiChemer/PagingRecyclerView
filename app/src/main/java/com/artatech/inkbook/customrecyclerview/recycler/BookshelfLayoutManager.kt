package com.artatech.inkbook.customrecyclerview.recycler

import android.content.Context
import androidx.recyclerview.widget.GridLayoutManager

class BookshelfLayoutManager(context: Context, spanCount: Int = 1) : GridLayoutManager(context, spanCount) {
    var isScrollEnabled : Boolean = false

    override fun canScrollHorizontally(): Boolean {
        return isScrollEnabled
    }

    override fun canScrollVertically(): Boolean {
        return isScrollEnabled
    }

    companion object {
        val LAYOUT_MANAGER_LIST = 0
        val LAYOUT_MANAGER_GRID = 1
    }
}