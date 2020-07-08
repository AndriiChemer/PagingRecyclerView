package com.artatech.inkbook.customrecyclerview.activities.custom

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.artatech.inkbook.customrecyclerview.MainModel
import com.artatech.inkbook.customrecyclerview.R
import com.artatech.inkbook.customrecyclerview.custom.PagingAdapter
import com.bumptech.glide.Glide

class MainAdapter: PagingAdapter<MainModel, MainAdapter.MainViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        return if (isModeGrid()) {
            val itemView = LayoutInflater
                .from(parent.context)
                .inflate(R.layout.main_item_grid, parent, false)
            MainViewHolder(itemView)

        } else {
            val itemView = LayoutInflater
                .from(parent.context)
                .inflate(R.layout.main_item, parent, false)
            MainViewHolder(itemView)

        }
    }

    class MainViewHolder(itemView: View): PagingMainViewHolder<MainModel>(itemView) {

        override fun bind(model: MainModel, position: Int) {
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
}