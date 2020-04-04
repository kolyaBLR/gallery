package com.androidcodeman.simpleimagegallery.recycler

import android.view.View
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.androidcodeman.simpleimagegallery.fragments.EditorFragment

class ImageHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val fragment = EditorFragment()

    init {
        val size = (view.context as? AppCompatActivity)?.window?.decorView?.width ?: 0
        view.layoutParams = FrameLayout.LayoutParams(size, size)
    }

    fun bind(data: Data) {
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