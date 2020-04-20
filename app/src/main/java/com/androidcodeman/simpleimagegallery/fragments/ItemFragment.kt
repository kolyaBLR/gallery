package com.androidcodeman.simpleimagegallery.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.androidcodeman.simpleimagegallery.DetailActivity
import com.androidcodeman.simpleimagegallery.R
import com.androidcodeman.simpleimagegallery.json.Post
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_image.*
import kotlinx.android.synthetic.main.fragment_image.view.*
import java.io.File
import kotlin.math.roundToInt

class ItemFragment : Fragment() {

    var data: Post? = null
        set(value) {
            field = value
            bindView()
        }

    var position = -1

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_image, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.image.layoutParams = view.image.layoutParams.apply {
            val size = getSize()
            this.width = size
            this.height = (size.div(16.0) * 9.0).roundToInt()
        }

        view.setOnClickListener {
            val intent = Intent(view.context, DetailActivity::class.java)
                    .putExtra("data", data)
                    .putExtra("width", getSize())
            this.startActivity(intent)
        }
        deleteButton.setOnClickListener {
            val post = this.data ?: return@setOnClickListener
            (context as? Listener)?.onDeleteClick(post)
        }
        bindView()
    }

    private fun getSize() = (view?.context as? AppCompatActivity)?.window?.decorView?.width ?: 0

    private fun bindView() {
        view ?: return
        val data = data ?: return

        if (data.imageUrl != null && data.imageUrl != "") {
            image.visibility = View.VISIBLE
            Picasso.get().load(File(data.imageUrl)).into(image)
        } else {
            image.visibility = View.GONE
        }

        titleItem.text = data.title
        descriptionItem.text = data.description
    }

    interface Listener {
        fun onDeleteClick(item: Post)
    }
}
