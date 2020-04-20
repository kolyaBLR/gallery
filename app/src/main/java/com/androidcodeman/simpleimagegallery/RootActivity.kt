package com.androidcodeman.simpleimagegallery

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.androidcodeman.simpleimagegallery.fragments.ItemFragment
import com.androidcodeman.simpleimagegallery.json.JsonData
import com.androidcodeman.simpleimagegallery.json.Post
import com.androidcodeman.simpleimagegallery.recycler.ImagesAdapter
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_root.*

class RootActivity : BaseActivity(), ItemFragment.Listener {

    private lateinit var jsonData: JsonData
    private lateinit var privateStorage: SharedPreferences
    private lateinit var publicStorage: SharedPreferences
    private val gson = Gson()
    private val adapter = ImagesAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_root)

        if (!getSessionViewModel().isAuth()) {
            signOut()
        } else {
            adapter.items = ArrayList(initData())

            imagesRecycler.layoutManager = LinearLayoutManager(this)
            imagesRecycler.adapter = adapter

            addImageButton.setOnClickListener { onAddImageClick() }

            signOut.setOnClickListener { signOut() }
        }
    }

    private fun signOut() {
        getSessionViewModel().auth(null)
        val intent = Intent(this, AuthActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }

    private fun initData(): ArrayList<Post> {
        try {
            publicStorage = getSharedPreferences(getStorageKey("public"), Context.MODE_PRIVATE)
            privateStorage = EncryptedSharedPreferences.create(getStorageKey("private"),
                    MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC),
                    this,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM)
            val imagesJson = privateStorage.getString("images", """{"posts" : null}""")
            jsonData = gson.fromJson(imagesJson, JsonData::class.java)
            return jsonData.posts ?: arrayListOf()
        } catch (ex: Exception) {
            Toast.makeText(this, R.string.error_read_json, Toast.LENGTH_LONG).show()
            return arrayListOf()
        }
    }

    private fun saveData() {
        jsonData.posts = adapter.items
        val data = gson.toJson(jsonData, JsonData::class.java)
        publicStorage.edit().putString("images", data).apply()
        privateStorage.edit().putString("images", data).apply()
    }

    private fun getStorageKey(other: String): String {
        return "${getSessionViewModel().getUserName()}_images_$other"
    }

    private fun onAddImageClick() {
        imagesRecycler.scrollToPosition(0)
        startActivityForResult(Intent(this, PostCreateActivity::class.java), SELECT_IMAGE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == SELECT_IMAGE && resultCode == Activity.RESULT_OK && data != null) {
            imagesRecycler.post {
                val item = data.getSerializableExtra(PostCreateActivity.RESULT_KEY) as Post
                adapter.items.add(0, item)
                adapter.notifyItemInserted(0)
                saveData()
            }
        }
    }

    companion object {
        private const val SELECT_IMAGE = 1242
    }

    override fun onDeleteClick(item: Post) {
        val index = adapter.items.indexOf(item)
        if (index >= 0) {
            adapter.items.removeAt(index)
            adapter.notifyItemRemoved(index)
            saveData()
        }
    }
}
