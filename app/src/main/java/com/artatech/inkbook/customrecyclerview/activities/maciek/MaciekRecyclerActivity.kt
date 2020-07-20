package com.artatech.inkbook.customrecyclerview.activities.maciek

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.artatech.inkbook.customrecyclerview.MainModel
import com.artatech.inkbook.customrecyclerview.R
import com.artatech.inkbook.customrecyclerview.activities.inkbook.InkBookAdapter
import com.artatech.inkbook.customrecyclerview.inkbookrecycler.InkbookHorizontaltemDecoration
import com.artatech.inkbook.customrecyclerview.maciek.InkBookRecyclerView2
import kotlinx.android.synthetic.main.activity_maciek_recycleractivity.*

class MaciekRecyclerActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maciek_recycleractivity)

        val inkBookAdapter = InkBookAdapter()

        bookshelfRecyclerView2.apply {
            layoutManager = GridLayoutManager(this@MaciekRecyclerActivity, 3)
            adapter = inkBookAdapter
            addItemDecoration(InkbookHorizontaltemDecoration())
        }

        bookshelfRecyclerView2.setOnScrollPageListener(object :
            InkBookRecyclerView2.OnScrollPageListener {
            @SuppressLint("SetTextI18n")
            override fun onPageListener(recyclerView: RecyclerView, page: Int, pageCount: Int) {
                setText("$page/$pageCount")
            }
        })


        inkBookAdapter.setItems(prepareListItem())

        previewButton.setOnClickListener { bookshelfRecyclerView2.showPreviewPage() }
        nextButton.setOnClickListener { bookshelfRecyclerView2.showNextPage() }
    }

    private fun setText(s: String) {
        pageInfo.text = s
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
            )
//            MainModel(
//                "Title 13",
//                "Description 9",
//                "https://firebasestorage.googleapis.com/v0/b/cars-2f419.appspot.com/o/cars%2F0%2F0.jpg?alt=media&token=8088ea82-8aca-4e95-a7c6-eeaf3e1df2c1"
//            ),
//            MainModel(
//                "Title 14",
//                "Description 9",
//                "https://firebasestorage.googleapis.com/v0/b/cars-2f419.appspot.com/o/cars%2F0%2F0.jpg?alt=media&token=8088ea82-8aca-4e95-a7c6-eeaf3e1df2c1"
//            )
        )
    }

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, MaciekRecyclerActivity::class.java)
            context.startActivity(intent)
        }
    }
}