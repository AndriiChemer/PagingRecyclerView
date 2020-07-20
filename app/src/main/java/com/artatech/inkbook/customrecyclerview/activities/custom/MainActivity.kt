package com.artatech.inkbook.customrecyclerview.activities.custom

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import com.artatech.inkbook.customrecyclerview.MainModel
import com.artatech.inkbook.customrecyclerview.R
import com.artatech.inkbook.customrecyclerview.activities.inkbook.InkBookActivity
import com.artatech.inkbook.customrecyclerview.activities.maciek.MaciekRecyclerActivity
import com.artatech.inkbook.customrecyclerview.custom.PagingAdapter
import com.artatech.inkbook.customrecyclerview.custom.PagingRecyclerView
import com.artatech.inkbook.customrecyclerview.custom.SpacingItemDecoration
import kotlinx.android.synthetic.main.main_activity.*

class MainActivity : AppCompatActivity() {

    private val adapter = MainAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        val layoutManager = PagingRecyclerView.CustomLayoutManager(this)
        val itemDecoration = SpacingItemDecoration()

        customRecyclerView.setLayoutManager(layoutManager,3)
        customRecyclerView.setAdapter(adapter)
        customRecyclerView.setItemDecoration(itemDecoration)


        adapter.setItems(prepareListItem(), object : PagingAdapter.PagingClickListener<MainModel> {
            override fun onItemClick(model: MainModel, index: Int) {
                Toast.makeText(this@MainActivity, "$index. ${model.title}", Toast.LENGTH_LONG).show()
            }
        })
        prepareListeners()
    }

    private fun prepareListeners() {

        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                adapter.filter.filter(newText)
                return false
            }

        })

        toogleLayoutManagerButton.setOnClickListener {
            if (customRecyclerView.isListMode()) {
                //set grid
                toogleLayoutManagerButton.setImageResource(R.drawable.ic_list)
                setGridLayoutManager()
            } else {
                //set list
                toogleLayoutManagerButton.setImageResource(R.drawable.ic_grid)
                setLinearLayoutManager()
            }
        }

        inkBookRecyclerButton.setOnClickListener { InkBookActivity.start(this) }
        maciekButton.setOnClickListener { MaciekRecyclerActivity.start(this) }

        preview.setOnClickListener { adapter.showPreviewPage() }
        next.setOnClickListener { adapter.showNextPage() }
    }

    private fun setLinearLayoutManager() {
        val layoutManager = PagingRecyclerView.CustomLayoutManager(this)
        val itemDecoration = SpacingItemDecoration()

        customRecyclerView.setLayoutManager(layoutManager,3)
        customRecyclerView.setAdapter(adapter)
        customRecyclerView.setItemDecoration(itemDecoration)

    }

    private fun setGridLayoutManager() {
        val spanCount = 3
        val spacing = 10 //px

        val layoutManager = PagingRecyclerView.CustomLayoutManager(this, spanCount)
        val itemDecoration = SpacingItemDecoration(spanCount, spacing)

        customRecyclerView.setLayoutManager(layoutManager,2)
        customRecyclerView.setAdapter(adapter)
        customRecyclerView.setItemDecoration(itemDecoration)
    }

    private fun prepareListItem(): List<MainModel> {
        return mutableListOf(
            MainModel(
                "Title 1",
                "Description 1",
                "https://firebasestorage.googleapis.com/v0/b/cars-2f419.appspot.com/o/cars%2F0%2F0.jpg?alt=media&token=8088ea82-8aca-4e95-a7c6-eeaf3e1df2c1"
            ),
            MainModel(
                "Title 2",
                "Description 2",
                "https://firebasestorage.googleapis.com/v0/b/cars-2f419.appspot.com/o/cars%2F1%2F0.jpg?alt=media&token=d0f72cc7-756c-4fc2-9fc2-16b72161868c"
            ),
            MainModel(
                "Title 3",
                "Description 3",
                "https://firebasestorage.googleapis.com/v0/b/cars-2f419.appspot.com/o/cars%2F3%2F0.jpg?alt=media&token=52833fd2-ae97-440a-a3eb-5112d1768952"
            ),
            MainModel(
                "Title 4",
                "Description 4",
                "https://firebasestorage.googleapis.com/v0/b/cars-2f419.appspot.com/o/cars%2F2%2F0.jpg?alt=media&token=8687a966-b5c9-4634-82a0-419f70f7d111"
            ),
            MainModel(
                "Title 5",
                "Description 5",
                "https://firebasestorage.googleapis.com/v0/b/cars-2f419.appspot.com/o/cars%2F4%2F0.jpg?alt=media&token=ce50b6d6-11d6-47fe-9caf-e3e599366a38"
            ),
            MainModel(
                "Title 6",
                "Description 6",
                "https://firebasestorage.googleapis.com/v0/b/cars-2f419.appspot.com/o/cars%2F5%2F0.jpg?alt=media&token=c6232629-cc8c-49f2-a06e-6dae037c4afe"
            ),
            MainModel(
                "Title 7",
                "Description 7",
                "https://firebasestorage.googleapis.com/v0/b/cars-2f419.appspot.com/o/cars%2F6%2F0.jpg?alt=media&token=88d0e807-1a23-4e22-8fff-fe3c315cc972"
            ),
            MainModel(
                "Title 8",
                "Description 8",
                "https://firebasestorage.googleapis.com/v0/b/cars-2f419.appspot.com/o/cars%2F7%2F0.jpg?alt=media&token=c014ee96-e8ea-4ef3-a296-68628a857d7b"
            ),
            MainModel(
                "Title 9",
                "Description 9",
                "https://firebasestorage.googleapis.com/v0/b/cars-2f419.appspot.com/o/cars%2F0%2F0.jpg?alt=media&token=8088ea82-8aca-4e95-a7c6-eeaf3e1df2c1"
            ),
            MainModel(
                "Title 10",
                "Description 9",
                "https://firebasestorage.googleapis.com/v0/b/cars-2f419.appspot.com/o/cars%2F0%2F0.jpg?alt=media&token=8088ea82-8aca-4e95-a7c6-eeaf3e1df2c1"
            ),
            MainModel(
                "Title 11",
                "Description 9",
                "https://firebasestorage.googleapis.com/v0/b/cars-2f419.appspot.com/o/cars%2F0%2F0.jpg?alt=media&token=8088ea82-8aca-4e95-a7c6-eeaf3e1df2c1"
            ),
            MainModel(
                "Title 12",
                "Description 9",
                "https://firebasestorage.googleapis.com/v0/b/cars-2f419.appspot.com/o/cars%2F0%2F0.jpg?alt=media&token=8088ea82-8aca-4e95-a7c6-eeaf3e1df2c1"
            ),
            MainModel(
                "Title 13",
                "Description 9",
                "https://firebasestorage.googleapis.com/v0/b/cars-2f419.appspot.com/o/cars%2F0%2F0.jpg?alt=media&token=8088ea82-8aca-4e95-a7c6-eeaf3e1df2c1"
            ),
            MainModel(
                "Title 14",
                "Description 9",
                "https://firebasestorage.googleapis.com/v0/b/cars-2f419.appspot.com/o/cars%2F0%2F0.jpg?alt=media&token=8088ea82-8aca-4e95-a7c6-eeaf3e1df2c1"
            )
        )
    }

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, MainActivity::class.java)
            context.startActivity(intent)
        }
    }
}