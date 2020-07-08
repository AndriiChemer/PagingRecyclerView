package com.artatech.inkbook.customrecyclerview.custom

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.artatech.inkbook.customrecyclerview.MainModel
import com.artatech.inkbook.customrecyclerview.R
import com.bumptech.glide.Glide

class PagingAdapter: RecyclerView.Adapter<PagingAdapter.MainViewHolder>() {

    private var pagingRecyclerView: PagingRecyclerView? = null
    private var listener: Listener? = null
    private val itemsPerPage by lazy { mutableListOf<MainModel>() }
    private val allItems by lazy { mutableListOf<MainModel>() }
    private var pageList: ArrayList<ArrayList<MainModel>> = ArrayList()
    private var currentPageIndex: Int = -1
    private var lastPageIndex: Int = -1

    fun setItems(items: List<MainModel>, listener: Listener? = null) {
        this.listener = listener
        setListener(listener)

        val itemPerPage = calculateItemPerPage()
        if (items.isNotEmpty()) {
            this.allItems.clear()
        }

        this.allItems.addAll(items)

        //Calculate pages
        val calculatedPages = CalculatePage.calculatePages(items as ArrayList<MainModel>, itemPerPage)
        pageList.addAll(calculatedPages)

        //Set page data
        itemsPerPage.addAll(calculatedPages.first())
        currentPageIndex = 1
        lastPageIndex = pageList.size
        this.listener?.reloadPageInfo("$currentPageIndex/$lastPageIndex")

        notifyDataSetChanged()
    }

    private fun calculateItemPerPage(): Int {
        return if (pagingRecyclerView != null) {
            pagingRecyclerView!!.getCustomLayoutManager().getItemPerPage()
        } else {
            3
        }
    }

    fun update() {
        val itemPerPage = calculateItemPerPage()
        val calculatedPages = CalculatePage.calculatePages(allItems as ArrayList<MainModel>, itemPerPage)

        pageList.clear()
        itemsPerPage.clear()
        pageList.addAll(calculatedPages)
        if (calculatedPages.isNotEmpty()) {
            itemsPerPage.addAll(calculatedPages.first())
        }
        currentPageIndex = 1
        lastPageIndex = pageList.size
        this.listener?.reloadPageInfo("$currentPageIndex/$lastPageIndex")
    }

    fun showNextPage() {
        if (currentPageIndex < lastPageIndex) {
            currentPageIndex++

            reload(currentPageIndex)

            notifyDataSetChanged()
        } else {
            Log.d("ANDRII", "It's last page!")
        }
    }

    fun showPreviewPage() {
        if (currentPageIndex != 1) {
            currentPageIndex--
            reload(currentPageIndex)

            notifyDataSetChanged()
        } else {
            Log.d("ANDRII", "It's first page!")
        }
    }

    private fun reload(pageIndex: Int) {

        listener?.reloadPageInfo("$pageIndex/$lastPageIndex")

        val newPageItems: ArrayList<MainModel> = pageList[pageIndex - 1]
        itemsPerPage.clear()
        itemsPerPage.addAll(newPageItems)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {

        return if (pagingRecyclerView?.getLayoutManagerViewType() == PagingRecyclerView.CustomLayoutManager.LAYOUT_MANAGER_LIST) {
            val itemView = LayoutInflater
                .from(parent.context)
                .inflate(R.layout.main_item, parent, false)
            MainViewHolder(itemView)

        } else {
            val itemView = LayoutInflater
                .from(parent.context)
                .inflate(R.layout.main_item_grid, parent, false)
            MainViewHolder(itemView)
        }
    }

    override fun getItemCount() = itemsPerPage.size

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        val item: MainModel = itemsPerPage[position]
        holder.bind(item)
    }

    fun setRecyclerView(pagingRecyclerView: PagingRecyclerView) {
        this.pagingRecyclerView = pagingRecyclerView
    }

    fun setListener(listener: Listener?) {
        if (listener != null) {
            this.listener = listener
        } else {
            this.listener = this.pagingRecyclerView?.listener
        }
        this.listener?.reloadPageInfo("$currentPageIndex/$lastPageIndex")
    }

    class MainViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        fun bind(model: MainModel) {
            val imageView = itemView.findViewById<ImageView>(R.id.imageView)
            val title = itemView.findViewById<TextView>(R.id.title)
            val description = itemView.findViewById<TextView>(R.id.description)

            Glide.with(itemView)
                .load(model.image)
                .dontAnimate()
                .into(imageView)

            title.text = model.title
            description.text = model.description

        }
    }

    interface Listener {
        fun reloadPageInfo(info: String)
    }
}