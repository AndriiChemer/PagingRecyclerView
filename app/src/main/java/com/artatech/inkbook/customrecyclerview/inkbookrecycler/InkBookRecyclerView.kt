package com.artatech.inkbook.customrecyclerview.inkbookrecycler


import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import android.view.ViewTreeObserver
import android.widget.LinearLayout
import androidx.recyclerview.widget.GridLayoutManager

import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.LinearLayoutManager;
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min


class InkBookRecyclerView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyle: Int = 0) : RecyclerView(context, attrs, defStyle) {
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

            mOnScrollPageListener?.onScrolled(this@InkBookRecyclerView,  pagePosition + 1, lastPagePosition + 1)
        }
    }
    private val mAdapterObserver = object : AdapterDataObserver() {
        override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
            super.onItemRangeInserted(positionStart, itemCount)
            adapter?.notifyItemChanged(max(0, positionStart - 1))
        }
    }

    interface OnScrollPageListener {
        fun onScrolled(recyclerView: RecyclerView, page: Int, pageCount: Int) = Unit
    }

    private val itemDecortion = object : RecyclerView.ItemDecoration() {
        override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: State) {
            super.getItemOffsets(outRect, view, parent, state)

            removeCallbacks(mOnScrollEventHandler)
            val position = max(0, parent.getChildAdapterPosition(view))
            val totalItemCount = adapter?.itemCount ?: 0;
            val page = if (position < mMeasuredPageItems) 0 else position / mMeasuredPageItems
            var top = 0;
            var bottom = mVerticalDividerHeight
            var left = 0
            var right = 0
            val spanCount =  ((parent as InkBookRecyclerView).layoutManager as? GridLayoutManager)?.spanCount ?: 1;

            if (spanCount > 1) {
                val column = position % spanCount
                val row =  if ((position - (page * mMeasuredPageItems)) < (mMeasuredPageItems / 2)) 0 else 1

                left = column * mHorizontalDividerHeight / spanCount
                right = mHorizontalDividerHeight - (column + 1) * mHorizontalDividerHeight / spanCount

                if (row == 0) {
                    top = mVerticalDividerHeight
                }
                if (position == totalItemCount - 1 || (row == 0 && position + spanCount- column > totalItemCount - 1)) {
                    bottom += abs(row - 1) * mMeasuredItemHeigh + mVerticalDividerHeight
                }
            } else {
                val lastPageFirstIndex = if (totalItemCount % mMeasuredPageItems == 0) totalItemCount - mMeasuredPageItems  else  (totalItemCount / mMeasuredPageItems) * mMeasuredPageItems
                val sizeOnPage = if (position < lastPageFirstIndex) mMeasuredPageItems else totalItemCount - lastPageFirstIndex
                val lastIndexOnPage = (page + 1) * mMeasuredPageItems - 1
                val firstIndexOnPage = lastIndexOnPage - mMeasuredPageItems + 1

                if (position == firstIndexOnPage) {
                    top += mVerticalDividerHeight
                }
                if (position == totalItemCount - 1) {
                    bottom += (mMeasuredPageItems - sizeOnPage) * (mMeasuredItemHeigh + mVerticalDividerHeight)
                }
            }


            val column = position % spanCount


            // spacing - column * ((1f / spanCount) * spacing)
//            val newLeft =
//                30 - column * 30 / spanCount
//            val newRight =
//                (column + 1) * 30 / spanCount

            outRect.set(0, top, 0, bottom)

            postDelayed(mOnScrollEventHandler, 250L)
        }
    }

    init {
        setHasFixedSize(true)

        addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                removeCallbacks(mOnScrollEventHandler)
                postDelayed(mOnScrollEventHandler, 250L)
            }
        })

        addItemDecoration(itemDecortion)
    }

    override fun setLayoutManager(layout: LayoutManager?) {
//        layoutManager?.apply {
//            if (spanCount == (layout as LayoutManagerr?)?.spanCount ?: spanCount) {
//                return
//            }
//        }
//
//        if (layout != null && layout !is LayoutManagerr) {
//            throw IllegalArgumentException("Only layout based on LayoutManagerr is supported.")
//        }

        super.setLayoutManager(layout)


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
            mHorizontalDividerHeight = (width - totalValue) / (max(1, rowItemsCount - 1)) // TODO - на +

            mMeasuredPageItems *= (layoutManager as? GridLayoutManager)?.spanCount ?: 1;
        }
    }

    fun setOnScrollPageListener(listener: OnScrollPageListener) {
        mOnScrollPageListener = listener
    }

    fun showNextPage() {
        scrollToPagePosition(pagePosition +1)
    }

    fun showPreviewPage() {
        scrollToPagePosition(pagePosition -1)
    }

    fun scrollToPagePosition(position: Int) {
        if (position in 0..lastPagePosition && position != pagePosition) {
            val totalItemCount = adapter?.itemCount ?: 0;
            var requiredItemPosition = min((position + 1) * mMeasuredPageItems - 1, totalItemCount - 1)
            if (position < pagePosition) {
                requiredItemPosition = max(position * mMeasuredPageItems, 0)
            }

            //layoutManager?.isScrollEnabled = true



            if((pagePosition== lastPagePosition || pagePosition == lastPagePosition )&&totalItemCount%mMeasuredPageItems!=0)
            {
                smoothScrollToPosition(requiredItemPosition)
            }
            else
            {
                scrollToPosition(requiredItemPosition);
            }

            //scrollToPagePosition(requiredItemPosition);

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
        val TAG = InkBookRecyclerView::class.java.simpleName
    }
}

fun <T : View> T.onSizeAvailable(function: (width : Int, height : Int) -> Unit) {
    if (height == 0 || width == 0)
        viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                viewTreeObserver.removeOnGlobalLayoutListener(this)
                function(width, height)
            }
        })
    else function(width, height)
}