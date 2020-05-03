package com.androidcodeman.simpleimagegallery

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.androidcodeman.simpleimagegallery.json.Post
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_detail.*
import java.io.File
import kotlin.math.roundToInt

class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        image.layoutParams = image.layoutParams.apply {
            val size = intent.getIntExtra("width", 0)
            this.width = size
            this.height = (size.div(16.0) * 9.0).roundToInt()
        }

        val data = intent.getSerializableExtra("data") as Post

        if (data.imageUrl != null && data.imageUrl != "") {
            image.visibility = View.VISIBLE
            if (data.isLocal) {
                Picasso.get().load(File(data.imageUrl)).into(image)
            } else {
                Picasso.get().load(data.imageUrl).into(image)
            }
        } else {
            image.visibility = View.GONE
        }
        titleItem.text = data.title
        descriptionItem.text = data.description
    }
}
