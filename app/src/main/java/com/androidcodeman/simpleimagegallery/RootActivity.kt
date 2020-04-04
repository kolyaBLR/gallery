package com.androidcodeman.simpleimagegallery

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.androidcodeman.simpleimagegallery.fragments.EditorFragment
import com.androidcodeman.simpleimagegallery.json.JsonData
import com.androidcodeman.simpleimagegallery.recycler.Data
import com.androidcodeman.simpleimagegallery.recycler.ImagesAdapter
import com.androidcodeman.simpleimagegallery.shit.MainActivity
import com.androidcodeman.simpleimagegallery.shit.fragments.pictureBrowserFragment
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_root.*
import kotlinx.android.synthetic.main.fragment_image.*
import java.lang.Exception

class RootActivity : BaseActivity(), EditorFragment.Listener {

    private lateinit var images: java.util.ArrayList<String>
    private lateinit var imagesStorage: SharedPreferences
    private val gson = Gson()
    private val adapter = ImagesAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_root)

        images = initData()
        adapter.items = ArrayList(images.map { Data(it, false) })

        imagesRecycler.layoutManager = LinearLayoutManager(this)
        imagesRecycler.adapter = adapter

        addImageButton.setOnClickListener { onAddImageClick() }

        signOut.setOnClickListener {
            getSessionViewModel().auth(null)
            val intent = Intent(this, AuthActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

        if (!getSessionViewModel().isAuth()) {
            signOut.callOnClick()
        }
    }

    private fun initData(): ArrayList<String> {
        try {
            imagesStorage = getSharedPreferences("images", Context.MODE_PRIVATE)
            val imagesJson = imagesStorage.getString("images", """{"images" : null}""")
            return gson.fromJson(imagesJson, JsonData::class.java).images ?: arrayListOf()
        } catch (ex: Exception) {
            Toast.makeText(this, R.string.error_read_json, Toast.LENGTH_LONG).show()
            return arrayListOf()
        }
    }

    private fun saveData(url: String?) {
        images.add(0, url ?: "")
        imagesStorage.edit().putString("images", gson.toJson(JsonData(images))).apply()
    }

    private fun onAddImageClick() {
        adapter.items.add(0, Data(null, true))
        adapter.notifyItemInserted(0)
        addImageButton.visibility = View.INVISIBLE
        imagesRecycler.scrollToPosition(0)
    }

    override fun onSaveCLick() {
        val url = adapter.items[0].imageUrl
        saveData(url)
        val item = Data(url, false)
        adapter.items[0] = item
        getFirstFragment()?.data = item
        addImageButton.visibility = View.VISIBLE
    }

    override fun onReselectedCLick() {
        startActivityForResult(Intent(this, MainActivity::class.java), SELECT_IMAGE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == SELECT_IMAGE && resultCode == Activity.RESULT_OK && data != null) {
            imagesRecycler.post {
                val item = Data(data.getStringExtra(pictureBrowserFragment.IMAGE_KEY), true)
                adapter.items[0] = item
                getFirstFragment()?.data = item
            }
        }
    }

    private fun getFirstFragment(): EditorFragment? {
        return supportFragmentManager.fragments.find { (it as? EditorFragment)?.position == 0 } as? EditorFragment
    }

    companion object {
        private const val SELECT_IMAGE = 1242
    }
}
