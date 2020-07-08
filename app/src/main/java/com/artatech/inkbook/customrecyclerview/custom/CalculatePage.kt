package com.artatech.inkbook.customrecyclerview.custom

class CalculatePage {
    companion object {
        fun <T> calculatePages(items: ArrayList<T>, itemPerPage: Int = 3): ArrayList<ArrayList<T>> {
            val itemsLength = items.size
            val pageList = ArrayList<ArrayList<T>>()

            var page: ArrayList<T> = ArrayList()
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