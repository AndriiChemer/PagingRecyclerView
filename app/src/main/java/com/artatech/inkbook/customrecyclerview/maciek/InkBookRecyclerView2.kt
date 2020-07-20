package com.artatech.inkbook.customrecyclerview.maciek


import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import androidx.recyclerview.widget.GridLayoutManager

import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.LinearLayoutManager;
import pl.inkcompat.Inkbookrecycler.onSizeAvailable
import kotlin.math.max
import kotlin.math.min


class InkBookRecyclerView2 @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyle: Int = 0) : RecyclerView(context, attrs, defStyle) {
    private var mMeasuredPageItems = 0
    private var mMeasuredItemHeigh = 0
    private var mMeasuredItemWidth = 0
    private var mOnScrollPageListener : OnScrollPageListener? = null
    private var mVerticalDividerHeight = 0
    private var mHorizontalDividerHeight = 0

    var pagePosition : Int = 0
        private set
    var lastPagePosition : Int = 0
        private set

    private val mOnScrollEventHandler = Runnable {
        (layoutManager as LinearLayoutManager).apply {
            val firstIndex = max(0, findFirstCompletelyVisibleItemPosition())
            val lastIndex = max(0, findLastCompletelyVisibleItemPosition())
            val totalItemCount = adapter?.itemCount ?: 0;

            pagePosition = firstIndex / mMeasuredPageItems
            lastPagePosition = if (totalItemCount - 1 < mMeasuredPageItems) 0 else (totalItemCount - 1) / mMeasuredPageItems

            if (totalItemCount -1 == lastIndex) {
                pagePosition = lastPagePosition
            }

            mOnScrollPageListener?.onPageListener(this@InkBookRecyclerView2,  pagePosition + 1, lastPagePosition + 1)

            (layoutManager as InkBookLayoutManager).isScrollEnabled = false
        }
    }
    private val mAdapterObserver = object : AdapterDataObserver() {
        override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
            super.onItemRangeInserted(positionStart, itemCount)
            adapter?.notifyItemChanged(max(0, positionStart - 1))
        }
    }

    interface OnScrollPageListener {
        fun onPageListener(recyclerView: RecyclerView, page: Int, pageCount: Int) = Unit
    }

    private val itemDecortion = object : ItemDecoration() {
        override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: State) {

            super.getItemOffsets(outRect, view, parent, state)

            removeCallbacks(mOnScrollEventHandler)
            val position = max(0, parent.getChildAdapterPosition(view))
            val totalItemCount = adapter?.itemCount ?: 0;
            var top = 0;
            var bottom = mVerticalDividerHeight
            val left = 0
            val right = 0
            val spanCount =  ((parent as InkBookRecyclerView2).layoutManager as? GridLayoutManager)?.spanCount ?: 1;

            val rowsOnPage = mMeasuredPageItems/spanCount;
            val row = position / spanCount;
            val lastRow = totalItemCount/ spanCount;


                if (row % rowsOnPage == 0) {
                    top += mVerticalDividerHeight
                }
                if(row == lastRow)
                {
                    val numberOfEmptySpacesToBottom = rowsOnPage - (row % rowsOnPage) -1;
                    bottom =  numberOfEmptySpacesToBottom * (mMeasuredItemHeigh + mVerticalDividerHeight);
                }


            outRect.set(left, top, right, bottom)

            postDelayed(mOnScrollEventHandler, 250L)
        }
    }

    init {
        setHasFixedSize(true)

        addOnScrollListener(object : OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                removeCallbacks(mOnScrollEventHandler)
                postDelayed(mOnScrollEventHandler, 250L)
            }
        })

        addItemDecoration(itemDecortion)
    }



    override fun setLayoutManager(layout: LayoutManager?) {
        super.setLayoutManager(InkBookLayoutManager(layout!!, context))

        onSizeAvailable { width, height ->
            onMeasureItems()
            swapAdapter(adapter, true)
        }
    }

    fun removeAndAddDecoration()
    {
        removeItemDecoration(itemDecortion);
        addItemDecoration(itemDecortion);
    }

    override fun getLayoutManager(): LayoutManager? {
        return super.getLayoutManager() as LayoutManager?
    }

    private fun onMeasureItems() {
        layoutManager?.let {
            val container = LinearLayout(context).apply {
                orientation = LinearLayout.VERTICAL
                layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
            }

            val itemView = adapter!!.onCreateViewHolder(container, adapter!!.getItemViewType(0)).itemView;
            val parent = LinearLayout(context).apply {
                orientation = LinearLayout.VERTICAL
                layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
                addView(itemView)
                measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED)
            }

            mMeasuredItemWidth = parent.measuredWidth
            mMeasuredItemHeigh = parent.measuredHeight
            mMeasuredPageItems = 0

            var totalValue = 0
            do {
                totalValue += itemView.measuredHeight
                ++mMeasuredPageItems
            } while (height - totalValue - itemView.measuredHeight >= 0)
            mVerticalDividerHeight = (height - totalValue) / (mMeasuredPageItems + 1)

            totalValue = 0
            var rowItemsCount = 0
            do {
                totalValue += itemView.measuredWidth
                ++rowItemsCount
            } while (width - totalValue - itemView.measuredWidth >= 0)
            mHorizontalDividerHeight = (width - totalValue) / (max(1, rowItemsCount - 1))

            mMeasuredPageItems *= (layoutManager as? GridLayoutManager)?.spanCount ?: 1;
        }
    }

    fun setOnScrollPageListener(listener : OnScrollPageListener) {
        mOnScrollPageListener = listener
    }

    fun showNextPage() {
        scrollToPagePosition(pagePosition +1)
    }

    fun showPreviewPage() {
        scrollToPagePosition(pagePosition -1)
    }

    private fun scrollToPagePosition(position: Int) {
        if (position in 0..lastPagePosition && position != pagePosition) {
            val totalItemCount = adapter?.itemCount ?: 0;
            var requiredItemPosition = min((position + 1) * mMeasuredPageItems - 1, totalItemCount - 1)
            if (position < pagePosition) {
                requiredItemPosition = max(position * mMeasuredPageItems, 0)
            }


            (layoutManager as InkBookLayoutManager).isScrollEnabled = true;

            if((pagePosition== lastPagePosition  )&&totalItemCount%mMeasuredPageItems!=0) {
                scrollToPosition(requiredItemPosition)
            }
            else {
                scrollToPosition(requiredItemPosition)
            }
        }
    }

    override fun setAdapter(adapter: Adapter<*>?) {
        this.adapter?.unregisterAdapterDataObserver(mAdapterObserver)
        super.setAdapter(adapter)
        adapter?.registerAdapterDataObserver(mAdapterObserver)
    }


    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        layoutManager?.let {
            onMeasureItems()
        }
    }


    companion object {
        val TAG = InkBookRecyclerView2::class.java.simpleName
    }
}



private class InkBookLayoutManager(layoutManager : RecyclerView.LayoutManager, context: Context) : GridLayoutManager(context, layoutManager.getSpanCount()) {


    var isScrollEnabled : Boolean = false

    override fun canScrollHorizontally(): Boolean {
        return isScrollEnabled
    }

    override fun canScrollVertically(): Boolean {
        return isScrollEnabled
    }

}

fun RecyclerView.LayoutManager.getSpanCount() : Int{
    return  (this as? GridLayoutManager)?.spanCount ?: 1
}

