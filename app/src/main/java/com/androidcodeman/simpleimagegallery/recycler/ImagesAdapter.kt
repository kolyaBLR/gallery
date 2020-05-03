package com.androidcodeman.simpleimagegallery.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.androidcodeman.simpleimagegallery.R
import com.androidcodeman.simpleimagegallery.json.Post

class ImagesAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var items = arrayListOf<Post>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.fragment_image, parent, false)
        return ItemHolder(view)
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as? ItemHolder)?.bind(items[holder.adapterPosition])
    }
}