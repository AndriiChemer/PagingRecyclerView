package com.artatech.inkbook.customrecyclerview.recycler

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
//import com.artatech.android.inkbook.bookshelf.arch.paging.BookshelfPageDataSource
//import com.artatech.android.inkbook.bookshelf.model.Collection
//import com.artatech.inkbook.customrecyclerview.MainModel
//import com.artatech.provider.shared.books.metadata.Book
//import com.artatech.provider.shared.books.metadata.Member

class BookshelfViewModelFactory<T>(val application: Application, val factory: BookshelfPageDataSource.Factory<T>) : ViewModelProvider.AndroidViewModelFactory(application) {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
//        if (modelClass.isAssignableFrom(CollectionListViewModel::class.java)) {
//            return CollectionListViewModel(application, factory as BookshelfPageDataSource.Factory<Collection>) as T
//        } else if (modelClass.isAssignableFrom(BookListViewModel::class.java)) {
//            return BookListViewModel(application, factory as BookshelfPageDataSource.Factory<MainModel>) as T
//        } else if (modelClass.isAssignableFrom(MemberListViewModel::class.java)) {
//            return MemberListViewModel(application, factory as BookshelfPageDataSource.Factory<Member>) as T
//        }

        return super.create(modelClass)
    }
}