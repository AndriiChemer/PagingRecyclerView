package com.artatech.inkbook.customrecyclerview.custom

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView


//val spanCount = 3; // 3 columns
//val spacing = 50; // 50px
//val includeEdge = false;
//recyclerView.addItemDecoration(GridSpacingItemDecoration(spanCount, spacing, includeEdge));

class SpacingItemDecoration(
    private val spanCount: Int = 1,
    private val spacing: Int = 10,
    private val includeEdge: Boolean = true): RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val position: Int = parent.getChildAdapterPosition(view) // item position

        val column = position % spanCount // item column

        if (includeEdge) {
            outRect.left =
                spacing - column * spacing / spanCount // spacing - column * ((1f / spanCount) * spacing)
            outRect.right =
                (column + 1) * spacing / spanCount // (column + 1) * ((1f / spanCount) * spacing)
            if (position < spanCount) { // top edge
                outRect.top = spacing
            }
            outRect.bottom = spacing // item bottom
        } else {
            outRect.left = column * spacing / spanCount // column * ((1f / spanCount) * spacing)
            outRect.right =
                spacing - (column + 1) * spacing / spanCount // spacing - (column + 1) * ((1f /    spanCount) * spacing)
            if (position >= spanCount) {
                outRect.top = spacing // item top
            }
        }
    }
}