package com.androidcodeman.simpleimagegallery.recycler

import android.content.Intent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.androidcodeman.simpleimagegallery.BaseActivity
import com.androidcodeman.simpleimagegallery.DetailActivity
import com.androidcodeman.simpleimagegallery.json.Post
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_image.view.*
import java.io.File
import kotlin.math.roundToInt

class ItemHolder(view: View) : RecyclerView.ViewHolder(view) {

    var data: Post? = null
        set(value) {
            field = value
            bindView()
        }

    fun bind(post: Post) {
        data = post
        itemView.image.layoutParams = itemView.image.layoutParams.apply {
            val size = getSize()
            this.width = size
            this.height = (size.div(16.0) * 9.0).roundToInt()
        }

        itemView.setOnClickListener {
            val intent = Intent(itemView.context, DetailActivity::class.java)
                    .putExtra("data", data)
                    .putExtra("width", getSize())
            (itemView.context).startActivity(intent)
        }
        itemView.deleteButton.setOnClickListener {
            val post = this.data ?: return@setOnClickListener
            (itemView.context as? Listener)?.onDeleteClick(post)
        }
        itemView.editButton.setOnClickListener {
            val post = this.data ?: return@setOnClickListener
            (itemView.context as? Listener)?.onEditClick(post, adapterPosition)
        }
        bindView()

        (itemView.context as? BaseActivity)?.let {
            if (it.getSessionViewModel().isAdmin()) {
                itemView.deleteButton.visibility = View.VISIBLE
                itemView.editButton.visibility = View.VISIBLE
            } else {
                itemView.deleteButton.visibility = View.INVISIBLE
                itemView.editButton.visibility = View.INVISIBLE
            }
        }
    }

    private fun getSize() = (itemView.context as? AppCompatActivity)?.window?.decorView?.width ?: 0

    private fun bindView() {
        val data = data ?: return

        if (data.imageUrl != null && data.imageUrl != "") {
            itemView.image.visibility = View.VISIBLE
            if (data.isLocal) {
                Picasso.get().load(File(data.imageUrl)).into(itemView.image)
            } else {
                Picasso.get().load(data.imageUrl).into(itemView.image)
            }
        } else {
            itemView.image.visibility = View.GONE
        }

        itemView.titleItem.text = data.title
        itemView.descriptionItem.text = data.description
    }

    interface Listener {
        fun onDeleteClick(item: Post)
        fun onEditClick(item: Post, adapterPosition: Int)
    }
}
