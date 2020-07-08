package com.artatech.inkbook.customrecyclerview.recycler

import android.content.Context
import android.database.ContentObserver
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.paging.DataSource
import androidx.paging.PageKeyedDataSource
import kotlin.math.max

abstract class BookshelfPageDataSource<T>(private val cls: Class<T>, val context: Context, val args: Bundle) : PageKeyedDataSource<Int, T>()  {
    protected var mTotalItemCount = 0
    val totalItemCount : Int
        get() = mTotalItemCount
    private val mObserver : ContentObserver
    internal var offset : Int

    init {
        offset = args.getInt("offset", 0)
        Log.d("BookshelfPageDataSource", "offset: $offset")
        mObserver = BooksProviderContentObserver()
    }

    override fun invalidate() {
        Log.d("BookshelfPageDataSource", "invalidate()")
        context.contentResolver.unregisterContentObserver(mObserver)
        args.putInt("offset", offset)
        super.invalidate()
    }

    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, T>) {
        Log.d("BookshelfPageDataSource", "loadInitial(params: ${params.requestedLoadSize})")
        offset = max(offset, 0)
        var result = query(offset, params.requestedLoadSize, args)
        if (result.isEmpty() && totalItemCount > 0) {
            offset = max(totalItemCount - params.requestedLoadSize, 0)
            result = query(offset, params.requestedLoadSize, args)
        }

        val previousPageKey = if (result.isNotEmpty() && offset > 0) max(0, offset - params.requestedLoadSize) else null
        val nextPageKey = if (result.isNotEmpty() && result.size + offset < mTotalItemCount) offset + params.requestedLoadSize else null

        Log.d("BookshelfPageDataSource", "previusPageKey: $previousPageKey, nextPageKey: $nextPageKey")
        callback.onResult(result, offset, mTotalItemCount, previousPageKey, nextPageKey)
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, T>) {
        Log.d("BookshelfPageDataSource", "loadAfter(params: ${params.requestedLoadSize})")
        val result = query(params.key, params.requestedLoadSize, args).toMutableList()
        val adjacentPageKey = if ((result.size + params.key) < mTotalItemCount) params.key + params.requestedLoadSize else null
        callback.onResult(result, adjacentPageKey)
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, T>) {
        Log.d("BookshelfPageDataSource", "loadBefore(params: ${params.requestedLoadSize})")
        val result = query(params.key, params.requestedLoadSize, args).toMutableList()
        val adjacentPageKey = if (params.key >= params.requestedLoadSize) params.key - params.requestedLoadSize else null
        callback.onResult(result, adjacentPageKey)
    }

    protected abstract fun query(offset: Int, limit: Int,  args : Bundle): List<T>

    private inner class BooksProviderContentObserver : ContentObserver(Handler(Looper.getMainLooper())) {

         override fun onChange(selfChange: Boolean, uri: Uri?) {
             super.onChange(selfChange, uri)
             invalidate()
         }
    }



    abstract class Factory<T>(private val args: Bundle = Bundle.EMPTY) : DataSource.Factory<Int, T>() {
        var arguments : Bundle = Bundle()

        override fun create(): BookshelfPageDataSource<T> {
            arguments.putAll(args)
            return create(arguments)
        }

        abstract fun create(args: Bundle): BookshelfPageDataSource<T>
    }
}