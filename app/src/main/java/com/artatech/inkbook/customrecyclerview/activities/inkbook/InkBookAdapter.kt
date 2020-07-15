package com.artatech.inkbook.customrecyclerview.activities.inkbook

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.artatech.inkbook.customrecyclerview.MainModel
import com.artatech.inkbook.customrecyclerview.R
import com.bumptech.glide.Glide

class InkBookAdapter: RecyclerView.Adapter<InkBookAdapter.InkBookViewHolder>() {

    private val items by lazy { mutableListOf<MainModel>() }

    fun setItems(items: List<MainModel>) {
        this.items.clear()
        this.items.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InkBookViewHolder {
        val itemView = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.main_item, parent, false)
        return InkBookViewHolder(itemView)
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: InkBookViewHolder, position: Int) {
        holder.onBind(items[position])
    }

    class InkBookViewHolder(view: View): RecyclerView.ViewHolder(view) {

        fun onBind(model: MainModel) {
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