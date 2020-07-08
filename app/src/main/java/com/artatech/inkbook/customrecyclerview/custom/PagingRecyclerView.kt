package com.artatech.inkbook.customrecyclerview.custom

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.artatech.inkbook.customrecyclerview.R
import kotlinx.android.synthetic.main.custom_recycler_view.view.*

class PagingRecyclerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr){

//    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
//    constructor(
//        context: Context,
//        attrs: AttributeSet?,
//        defStyleAttr: Int,
//        defStyleRes: Int)
//            : super(context, attrs, defStyleAttr, defStyleRes)

    private var adapter: PagingAdapter? = null

    val listener = object : PagingAdapter.Listener {
        override fun reloadPageInfo(info: String) {
            currentPageTextView.text = info
        }

    }

    init {
        LayoutInflater.from(context).inflate(R.layout.custom_recycler_view, this, true)

        attrs?.let {
            //TODO set default
//            val typedArray = context.obtainStyledAttributes(it, R.styleable.CustomRecyclerView, 0, 0)
//
//            val isBottomNavigationVisible = resources.getBoolean(typedArray.getResourceId(R.styleable.CustomRecyclerView_isNavigationVisible, 1))
//
//            if (isBottomNavigationVisible) {
//                view.visibilityGroup.visibility = View.VISIBLE
//                view.guideline.setGuidelinePercent(0.93f)
//            } else {
//                view.visibilityGroup.visibility = View.INVISIBLE
//                view.guideline.setGuidelinePercent(1f)
//            }
//
//
//            typedArray.recycle()
        }

        prepareListeners()

    }

    fun setLayoutManager(layoutManager: CustomLayoutManager, itemInColumn: Int = 3) {
        layoutManager.itemInColumn = itemInColumn
        recyclerView.layoutManager = layoutManager
    }

    fun getLayoutManagerViewType(): Int {
        return (recyclerView.layoutManager as CustomLayoutManager).getViewType()
    }

    fun setAdapter(adapter: PagingAdapter) {
        adapter.update()
        this.adapter = adapter

        adapter.setRecyclerView(this)
        recyclerView.adapter = adapter

    }

    fun getCustomLayoutManager(): CustomLayoutManager {
        return recyclerView.layoutManager as CustomLayoutManager
    }

    private fun prepareListeners() {
        previewButton.setOnClickListener { adapter?.showPreviewPage() }
        nextButton.setOnClickListener { adapter?.showNextPage() }
    }

    fun isListMode(): Boolean {
        return getLayoutManagerViewType() == CustomLayoutManager.LAYOUT_MANAGER_LIST
    }

    fun addItemDecoration(itemDecoration: RecyclerView.ItemDecoration) {
        if (recyclerView.itemDecorationCount == 0) {
            recyclerView.addItemDecoration(itemDecoration)
        } else if(recyclerView.itemDecorationCount == 1) {
            recyclerView.removeItemDecorationAt(0)
            recyclerView.addItemDecoration(itemDecoration)
        }
    }

    class CustomLayoutManager(context: Context, spanCount: Int = 1) : GridLayoutManager(context, spanCount) {
        var itemInRow: Int = spanCount
        var itemInColumn: Int = 1

        fun getItemPerPage(): Int {
            return if(spanCount == 1) itemInColumn else itemInRow * itemInColumn
        }

        fun getViewType(): Int {
            return if(spanCount == 1) LAYOUT_MANAGER_LIST else LAYOUT_MANAGER_GRID
        }

        companion object {
            val LAYOUT_MANAGER_LIST = 0
            val LAYOUT_MANAGER_GRID = 1
        }
    }
}