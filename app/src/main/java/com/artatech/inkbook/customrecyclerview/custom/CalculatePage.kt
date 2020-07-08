package com.artatech.inkbook.customrecyclerview.custom

import com.artatech.inkbook.customrecyclerview.MainModel

class CalculatePage {
    companion object {
        fun calculatePages(items: ArrayList<MainModel>, itemPerPage: Int = 3): ArrayList<ArrayList<MainModel>> {
            val itemsLength = items.size
            val pageList = ArrayList<ArrayList<MainModel>>()

            var page: ArrayList<MainModel> = ArrayList()
            var index = 1

            for (item in items) {
                if (index % itemPerPage == 0) {

                    page.add(item)
                    pageList.add(page)
                    page = ArrayList()
                } else if(index % itemPerPage != 0 && index == itemsLength) {
                    page.add(item)
                    pageList.add(page)
                } else {
                    page.add(item)
                }
                index++
            }

            return pageList
        }
    }
}