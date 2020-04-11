package com.androidcodeman.simpleimagegallery.recycler

import android.view.View
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.androidcodeman.simpleimagegallery.fragments.ItemFragment
import com.androidcodeman.simpleimagegallery.json.Post

class ImageHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val fragment = ItemFragment()

    fun bind(data: Post) {
        fragment.data = data
    }

    fun attach() {
        if (!fragment.isAdded) {
            (itemView.context as? AppCompatActivity)
                    ?.supportFragmentManager?.beginTransaction()
                    ?.add(itemView.id, fragment)
                    ?.commit()
            fragment.position = adapterPosition
        }
    }

    fun detach() {
        if (fragment.isAdded) {
            (itemView.context as? AppCompatActivity)
                    ?.supportFragmentManager?.beginTransaction()
                    ?.remove(fragment)
                    ?.commit()
            fragment.position = - 1
        }
    }
}