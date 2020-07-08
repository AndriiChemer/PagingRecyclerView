package com.artatech.inkbook.customrecyclerview.custom

import android.util.Log
import android.view.View
import androidx.recyclerview.widget.RecyclerView

abstract class PagingAdapter<T, VH: PagingAdapter.PagingMainViewHolder<T>> : RecyclerView.Adapter<VH>(), GeneralAdapterListener {

    private var pagingRecyclerView: PagingRecyclerView? = null
    private var listener: Listener? = null
    private val itemsPerPage by lazy { mutableListOf<T>() }
    private val allItems by lazy { mutableListOf<T>() }
    private var pageList: ArrayList<ArrayList<T>> = ArrayList()
    private var currentPageIndex: Int = -1
    private var lastPageIndex: Int = -1

    fun setItems(items: List<T>, listener: Listener? = null) {
        this.listener = listener
        setListener(listener)

        val itemPerPage = calculateItemPerPage()
        if (items.isNotEmpty()) {
            this.allItems.clear()
        }

        this.allItems.addAll(items)

        //Calculate pages
        val calculatedPages = CalculatePage.calculatePages(items as ArrayList<T>, itemPerPage)
        pageList.addAll(calculatedPages)

        //Set page data
        itemsPerPage.addAll(calculatedPages.first())
        currentPageIndex = 1
        lastPageIndex = pageList.size
        this.listener?.updatePageInfo(currentPageIndex, lastPageIndex)

        notifyDataSetChanged()
    }

    private fun calculateItemPerPage(): Int {
        return if (pagingRecyclerView != null) {
            pagingRecyclerView!!.getCustomLayoutManager().getItemPerPage()
        } else {
            3
        }
    }

    override fun update() {
        val itemPerPage = calculateItemPerPage()
        val calculatedPages = CalculatePage.calculatePages(allItems as ArrayList<T>, itemPerPage)

        pageList.clear()
        itemsPerPage.clear()
        pageList.addAll(calculatedPages)
        if (calculatedPages.isNotEmpty()) {
            itemsPerPage.addAll(calculatedPages.first())
        }
        currentPageIndex = 1
        lastPageIndex = pageList.size
        this.listener?.updatePageInfo(currentPageIndex, lastPageIndex)
    }

    override fun showNextPage() {
        if (currentPageIndex < lastPageIndex) {
            currentPageIndex++

            reload(currentPageIndex)

            notifyDataSetChanged()
        } else {
            Log.d("ANDRII", "It's last page!")
        }
    }

    override fun showPreviewPage() {
        if (currentPageIndex != 1) {
            currentPageIndex--
            reload(currentPageIndex)

            notifyDataSetChanged()
        } else {
            Log.d("ANDRII", "It's first page!")
        }
    }

    private fun reload(pageIndex: Int) {

        listener?.updatePageInfo(pageIndex, lastPageIndex)

        val newPageItems: ArrayList<T> = pageList[pageIndex - 1]
        itemsPerPage.clear()
        itemsPerPage.addAll(newPageItems)
    }

    fun isModeGrid(): Boolean {
        return pagingRecyclerView?.getLayoutManagerViewType() == PagingRecyclerView.CustomLayoutManager.LAYOUT_MANAGER_GRID
    }

    override fun getItemCount() = itemsPerPage.size

    override fun onBindViewHolder(holder: VH, position: Int) {
        val item: T = itemsPerPage[position]
        holder.bind(item, position)
    }

    override fun setRecyclerView(pagingRecyclerView: PagingRecyclerView) {
        this.pagingRecyclerView = pagingRecyclerView
    }

    private fun setListener(listener: Listener?) {
        if (listener != null) {
            this.listener = listener
        } else {
            this.listener = this.pagingRecyclerView?.listener
        }
        this.listener?.updatePageInfo(currentPageIndex, lastPageIndex)
    }

    abstract class PagingMainViewHolder<T>(itemView: View): RecyclerView.ViewHolder(itemView) {

        abstract fun bind(model: T, position: Int)
    }

    interface Listener {
        fun updatePageInfo(currentPage: Int, totalPage: Int)
    }
}