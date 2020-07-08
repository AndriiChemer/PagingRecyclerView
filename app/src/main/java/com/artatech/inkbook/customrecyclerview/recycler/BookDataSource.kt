package com.artatech.inkbook.customrecyclerview.recycler

import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
//import com.artatech.android.inkbook.bookshelf.R
//import com.artatech.android.inkbook.bookshelf.ext.asList
//import com.artatech.bookshelf.shared.BookStoreHelper
import com.artatech.inkbook.customrecyclerview.MainModel
//import com.artatech.provider.shared.books.BookStore
//import com.artatech.provider.shared.books.metadata.Book

//class BookDataSource(context: Context, args: Bundle) : BookshelfPageDataSource<MainModel>(MainModel::class.java, context, args) {

//    override fun query(offset: Int, limit: Int, args: Bundle): List<MainModel> {
//        Log.i("BookDataSource", "query() offset=$offset limit=$limit args=${args.getString(ARG_SEARCH_PATTERN)}")
//        var selection: String? = null
//        var selectionArgs: Array<String>? = null
//        val pattern = args.getString(ARG_SEARCH_PATTERN)

//        if (!pattern.isNullOrEmpty()) {
//            val patternAsInt = pattern.toIntOrNull()

//            selection = StringBuilder().apply {
//                append("beginGroup ")
//                append("${BookStore.Books.BookColumns.FILE_ID} contains ? insensitive")
//                append(" or ")
//                append("${BookStore.Books.BookColumns.BOOK_KEY} contains ? insensitive")
//                append(" or ")
//                append("${BookStore.Books.BookColumns.TITLE} contains ? insensitive")
//                append(" or ")
//                append("${BookStore.Books.BookColumns.DESCRIPTION} contains ? insensitive")
//                append(" or ")
//                append("${BookStore.Books.BookColumns.MIME_TYPE} contains ? insensitive")
//                append(" or ")
//                append("${BookStore.Books.BookColumns.IDENTIFIER} contains ? insensitive")
//                append(" or ")
//                append("${BookStore.Books.BookColumns.LANGUAGE} contains ? insensitive")
//                append(" or ")
//                append("${BookStore.Books.BookColumns.TAGS} contains ? insensitive")
//                append(" or ")
//
//                patternAsInt?.apply {
//                    append("${BookStore.Books.BookColumns.TOTAL_PAGE_COUNT} equalTo ?")
//                    append(" or ")
//                }
//
//                append("${BookStore.Books.BookColumns.PUBLISHER + "." + BookStore.Publishers.PublisherColumns.PUBLISHER_KEY} contains ? insensitive")
//                append(" or ")
//                append("${BookStore.Books.BookColumns.PUBLISHER + "." + BookStore.Publishers.PublisherColumns.PUBLISHER} contains ? insensitive")
//                append(" or ")
//                append("${BookStore.Books.BookColumns.AUTHORS + "." + BookStore.Authors.AuthorColumns.AUTHOR_KEY} contains ? insensitive")
//                append(" or ")
//                append("${BookStore.Books.BookColumns.AUTHORS + "." + BookStore.Authors.AuthorColumns.AUTHOR} contains ? insensitive")
//                append(" endGroup")
//            }.toString()

//            selectionArgs = Array(if (patternAsInt != null) 13 else 12) { pattern }
//        }

//        when (args.getInt(ARG_FILTER, -1)) {
//            R.id.action_show_favorites -> {
//                selection = if (TextUtils.isEmpty(selection)) {
//                    "${BookStore.Books.BookColumns.TAGS} contains ?"
//                } else {
//                    selection + " ${BookStore.Books.BookColumns.TAGS} contains ?"
//                }
//
//                selectionArgs = if (selectionArgs == null) {
//                    Array(1) {""}
//                } else {
//                    java.util.Arrays.copyOf(selectionArgs, selectionArgs.size + 1)
//                }
//                selectionArgs?.let {
//                    it[it.size - 1] = BookStoreHelper.TAG_FAVORITE
//                }
//            }
//            R.id.action_show_not_downloaded -> {
//
//            }
//            R.id.action_show_reading -> {
//                selection = if (TextUtils.isEmpty(selection)) {
//                    "${BookStore.Books.BookColumns.TAGS} contains ? not ${BookStore.Books.BookColumns.TAGS} contains ?"
//                } else {
//                    selection + " ${BookStore.Books.BookColumns.TAGS} contains ? not ${BookStore.Books.BookColumns.TAGS} contains ?"
//                }
//
//                selectionArgs = if (selectionArgs == null) {
//                    Array(2) {""}
//                } else {
//                    java.util.Arrays.copyOf(selectionArgs, selectionArgs.size + 2)
//                }
//                selectionArgs?.let {
//                    it[it.size - 2] = BookStoreHelper.TAG_READING
//                    it[it.size - 1] = BookStoreHelper.TAG_READ
//                }
//            }
//            R.id.action_show_unread -> {
//                selection = if (TextUtils.isEmpty(selection)) {
//                    "not ${BookStore.Books.BookColumns.TAGS} contains ?"
//                } else {
//                    selection + " not ${BookStore.Books.BookColumns.TAGS} contains ?"
//                }
//
//                selectionArgs = if (selectionArgs == null) {
//                    Array(1) {""}
//                } else {
//                    java.util.Arrays.copyOf(selectionArgs, selectionArgs.size + 1)
//                }
//                selectionArgs?.let {
//                    it[it.size - 1] = BookStoreHelper.TAG_READ
//                }
//            }
//        }

//        val sortOrder = StringBuilder().apply {
//            when(args.getInt(ARG_SORT, -1)) {
//                R.id.action_sort_by_author -> {
//                    append(BookStore.Books.BookColumns.AUTHORS_KEY)
//                }
//                R.id.action_sort_by_recents -> {
//                    append("${BookStore.Books.BookColumns.DATE_MODIFIED} DESC")
//                }
//                R.id.action_sort_by_title -> {
//                    append(BookStore.Books.BookColumns.TITLE)
//                }
//                R.id.action_sort_by_date -> {
//                    append("${BookStore.Books.BookColumns.DATE_CREATION} DESC")
//                }
//            }
//
//            append(" LIMIT $limit")
//            append(" OFFSET $offset")
//
//        }.toString()
//
//        Log.d("BookDataSource", "selection=$selection selectionArgs=$selectionArgs sortorder=$sortOrder")
//
//        val out = ArrayList<Book>()
//
//        context.contentResolver.acquireUnstableContentProviderClient(BookStore.Books.CONTENT_URI)?.apply {
//            val cursor = try {
//                query(BookStore.Books.CONTENT_URI,
//                        arrayOf(BookStore.Books.BookColumns._ID, BookStore.Books.BookColumns.FILE_ID, BookStore.Books.BookColumns.AUTHORS, BookStore.Books.BookColumns.TITLE, BookStore.Books.BookColumns.TAGS, BookStore.Books.BookColumns.MIME_TYPE, BookStore.Books.BookColumns.FLAGS, BookStore.Books.BookColumns.DATE_MODIFIED, BookStore.Books.BookColumns.DATE_CREATION, BookStore.Books.BookColumns.TOTAL_PAGE_COUNT),
//                        selection,
//                        selectionArgs,
//                        sortOrder)
//            } catch (e: Exception) {
//                null
//            }
//
//            cursor?.let {
//                mTotalItemCount = it.extras.getInt(BookStore.EXTRA_RESULT_COUNT, 0)
//                out.addAll(it.asList(Book::class.java))
//                it.close()
//            } ?: run {
//                mTotalItemCount = 0
//            }
//
//            release()
//        }

//        return out
//    }


//    class Factory(private val context: Context, args: Bundle = Bundle.EMPTY) : BookshelfPageDataSource.Factory<Book>(args) {
//        override fun create(args: Bundle): BookshelfPageDataSource<Book> {
//            return BookDataSource(context, args)
//        }
//    }


//    companion object {
//        const val ARG_SEARCH_PATTERN = "search_pattern"
//        const val ARG_FILTER = "filter"
//        const val ARG_SORT = "sort"
//    }
//}