package com.androidcodeman.simpleimagegallery.recycler

import android.os.Build
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.recyclerview.widget.RecyclerView
import com.androidcodeman.simpleimagegallery.json.Post

class ImagesAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var items = arrayListOf<Post>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = FrameLayout(parent.context)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            view.id = View.generateViewId()
        }
        return ImageHolder(view)
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as? ImageHolder)?.bind(items[holder.adapterPosition])
    }

    override fun onViewAttachedToWindow(holder: RecyclerView.ViewHolder) {
        super.onViewAttachedToWindow(holder)
        (holder as? ImageHolder)?.attach()
    }

    override fun onViewDetachedFromWindow(holder: RecyclerView.ViewHolder) {
        super.onViewDetachedFromWindow(holder)
        (holder as? ImageHolder)?.detach()
    }
}