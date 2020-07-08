package com.artatech.inkbook.customrecyclerview.custom

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.artatech.inkbook.customrecyclerview.R
import kotlinx.android.synthetic.main.custom_recycler_view.view.*

class PagingRecyclerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr){

    private var colorControlNormal: Int = R.color.colorGreyDisable
    private var adapter: GeneralAdapterListener? = null

    val listener = object : PagingAdapter.PagingListener {
        @SuppressLint("SetTextI18n")
        override fun updatePageInfo(currentPage: Int, totalPage: Int) {
            currentPageTextView.text = "$currentPage/$totalPage"
        }

        override fun toggleButtons(currentPage: Int, lastPage: Int) {
            when (currentPage) {
                lastPage -> {
                    disableNextButton()
                    enablePreviewButton()
                }
                1 -> {
                    disablePreviewButton()
                    enableNextButton()
                }
                else -> {
                    enableNextButton()
                    enablePreviewButton()
                }
            }
        }
    }

    init {
        LayoutInflater.from(context).inflate(R.layout.custom_recycler_view, this, true)

        attrs?.let {
            val attributes = context.obtainStyledAttributes(it, R.styleable.PagingRecyclerView, 0, 0)

            val isBottomNavigationVisible = attributes.getBoolean(R.styleable.PagingRecyclerView_isNavigationVisible, true)
            toggleBottomNavigation(isBottomNavigationVisible)


            attributes.recycle()
        }

        prepareListeners()
        prepareNormalColor()
    }

    private fun prepareNormalColor() {
        val typedValue = TypedValue()
        context.theme.resolveAttribute(R.attr.colorControlNormal, typedValue, true)
        this.colorControlNormal = typedValue.resourceId
    }

    private fun toggleBottomNavigation(isVisible: Boolean) {
        if (isVisible) {
            visibilityGroup.visibility = View.VISIBLE
            guideline.setGuidelinePercent(0.93f)
        } else {
            visibilityGroup.visibility = View.GONE
            guideline.setGuidelinePercent(1f)
        }
    }

    fun setLayoutManager(layoutManager: CustomLayoutManager, itemInColumn: Int = 3) {
        layoutManager.itemInColumn = itemInColumn
        recyclerView.layoutManager = layoutManager
    }

    fun getLayoutManagerViewType(): Int {
        return (recyclerView.layoutManager as CustomLayoutManager).getViewType()
    }

    fun setAdapter(adapter: GeneralAdapterListener) {
        adapter.update()
        this.adapter = adapter

        adapter.setRecyclerView(this)
        recyclerView.adapter = adapter as PagingAdapter<*, *>

    }

    fun getCustomLayoutManager(): CustomLayoutManager {
        return recyclerView.layoutManager as CustomLayoutManager
    }

    private fun prepareListeners() {
        previewButton.setOnClickListener {
            adapter?.showPreviewPage()
        }
        nextButton.setOnClickListener {
            adapter?.showNextPage()
        }

        recyclerView
    }

    fun isListMode(): Boolean {
        return getLayoutManagerViewType() == CustomLayoutManager.LAYOUT_MANAGER_LIST
    }

    fun setItemDecoration(itemDecoration: RecyclerView.ItemDecoration) {
        if (recyclerView.itemDecorationCount == 0) {
            recyclerView.addItemDecoration(itemDecoration)
        } else if(recyclerView.itemDecorationCount == 1) {
            recyclerView.removeItemDecorationAt(0)
            recyclerView.addItemDecoration(itemDecoration)
        }
    }

    private fun enableNextButton() {
        val drawableRight = ContextCompat.getDrawable(context, R.drawable.ic_arrow_right)
        nextButton.setCompoundDrawablesWithIntrinsicBounds(null, null, drawableRight, null)
        nextButton.setTextColor(ContextCompat.getColor(context, colorControlNormal))
    }

    private fun disableNextButton() {
        val drawableRight = ContextCompat.getDrawable(context, R.drawable.ic_arrow_right_disable)
        nextButton.setCompoundDrawablesWithIntrinsicBounds(null, null, drawableRight, null)
        nextButton.setTextColor(ContextCompat.getColor(context, R.color.colorGreyDisable))
    }

    private fun enablePreviewButton() {
        val drawableLeft = ContextCompat.getDrawable(context, R.drawable.ic_arrow_left)
        previewButton.setTextColor(ContextCompat.getColor(context, colorControlNormal))
        previewButton.setCompoundDrawablesWithIntrinsicBounds(drawableLeft, null, null, null)
    }

    private fun disablePreviewButton() {
        val drawableLeft = ContextCompat.getDrawable(context, R.drawable.ic_arrow_left_disable)
        previewButton.setCompoundDrawablesWithIntrinsicBounds(drawableLeft, null, null, null)
        previewButton.setTextColor(ContextCompat.getColor(context, R.color.colorGreyDisable))
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