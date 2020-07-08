package com.artatech.inkbook.customrecyclerview.custom

interface GeneralAdapterListener {
    fun setRecyclerView(pagingRecyclerView: PagingRecyclerView)
    fun showPreviewPage()
    fun showNextPage()
    fun update()
}