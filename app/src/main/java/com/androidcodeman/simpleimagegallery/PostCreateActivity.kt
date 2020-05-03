package com.androidcodeman.simpleimagegallery

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.androidcodeman.simpleimagegallery.json.Post
import com.androidcodeman.simpleimagegallery.shit.MainActivity
import com.androidcodeman.simpleimagegallery.shit.fragments.pictureBrowserFragment
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_post_creator.*
import kotlinx.android.synthetic.main.activity_post_creator.image
import java.io.File

class PostCreateActivity : AppCompatActivity() {

    private var post: Post? = null
    private var isLocal = false
    private var imageUrl: String? = null
        set(value) {
            field = value
            image?.let { image ->
                if (!isLocal) {
                    Picasso.get().load(field).into(image)
                } else {
                    Picasso.get().load(File(field)).into(image)
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_creator)

        post = intent.getSerializableExtra(POST) as? Post
        isLocal = post?.isLocal ?: false

        image.setOnClickListener {
            startActivityForResult(Intent(this, MainActivity::class.java), SELECT_IMAGE)
        }
        createPost.setText(if (post == null) R.string.create_new_post else R.string.save_post)
        createPost.setOnClickListener {
            val title = titleEditor.text
            val description = descriptionEditor.text
            if (imageUrl?.isNotEmpty() != true) {
                Toast.makeText(it.context, R.string.image_is_empty, Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            if (title?.isNotEmpty() != true) {
                Toast.makeText(it.context, R.string.title_is_enpty, Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            if (description?.isNotEmpty() != true) {
                Toast.makeText(it.context, R.string.description_is_enpty, Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            val isLocalActual = this.isLocal
            val data = Post().apply {
                this.imageUrl = this@PostCreateActivity.imageUrl
                this.title = title.toString()
                this.description = description.toString()
                this.isLocal = isLocalActual
            }
            setResult(Activity.RESULT_OK, Intent().putExtra(RESULT_KEY, data)
                    .putExtra(IS_EDIT, post != null)
                    .putExtra(POSITION, intent.getIntExtra(POSITION, 0))
            )
            Toast.makeText(it.context, if (post != null) R.string.post_edited else R.string.post_created, Toast.LENGTH_LONG).show()
            finish()
        }

        post?.let {
            imageUrl = it.imageUrl
            titleEditor.setText(it.title)
            descriptionEditor.setText(it.description)
            PostCreateActivity@ isLocal = this.isLocal
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == SELECT_IMAGE && resultCode == Activity.RESULT_OK && data != null) {
            isLocal = true
            imageUrl = data.getStringExtra(pictureBrowserFragment.IMAGE_KEY)
        }
    }

    companion object {
        const val POSITION = "position"
        private const val SELECT_IMAGE = 2311
        const val POST = "post"
        const val IS_EDIT = "is_edit"
        const val RESULT_KEY = "result_key"
    }
}
