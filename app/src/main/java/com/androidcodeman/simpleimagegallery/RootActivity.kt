package com.androidcodeman.simpleimagegallery

import android.app.Activity
import android.app.ProgressDialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.androidcodeman.simpleimagegallery.api.Result
import com.androidcodeman.simpleimagegallery.api.RetrofitInstance
import com.androidcodeman.simpleimagegallery.recycler.ItemHolder
import com.androidcodeman.simpleimagegallery.json.JsonData
import com.androidcodeman.simpleimagegallery.json.Post
import com.androidcodeman.simpleimagegallery.recycler.ImagesAdapter
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_root.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RootActivity : BaseActivity(), ItemHolder.Listener {

    private lateinit var dialog: ProgressDialog
    private lateinit var jsonData: JsonData
    private lateinit var privateStorage: SharedPreferences
    private lateinit var publicStorage: SharedPreferences
    private val gson = Gson()
    private val adapter = ImagesAdapter()
    private lateinit var networkService: Intent
    private var isRegister = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_root)
        dialog = ProgressDialog(this)
        networkService = Intent(this, NetworkService::class.java)

        if (!getSessionViewModel().isAuth()) {
            signOut()
        } else {
            adapter.items = ArrayList(initData())
            initNetworkPostsIfNeed()

            imagesRecycler.layoutManager = LinearLayoutManager(this)
            imagesRecycler.adapter = adapter

            addImageButton.setOnClickListener { onAddImageClick() }

            signOut.setOnClickListener { signOut() }

            if (getSessionViewModel().isAdmin()) {
                addImageButton.visibility = View.VISIBLE
            } else {
                addImageButton.visibility = View.INVISIBLE
            }
            registerReceiver(receiver, IntentFilter(NetworkService.ACTION))
        }
    }

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            (intent?.getSerializableExtra(NetworkService.RESULT) as? NetworkResult)?.items
                    ?.let { adapter.items.addAll(it) }
                    ?.let { saveData() }
                    ?.let { adapter.notifyDataSetChanged() }
                    ?.let {
                        if (dialog.isShowing) {
                            dialog.dismiss()
                        }
                    }
                    ?: context?.let {
                        Toast.makeText(it, R.string.error_loading, Toast.LENGTH_LONG).show()
                    }
        }
    }

    private fun initNetworkPostsIfNeed() {
        if (adapter.items.isNotEmpty()) {
            return
        }
        dialog.show()
        dialog.dismiss()
        isRegister = true
        startService(networkService)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (isRegister) {
            stopService(intent)
            unregisterReceiver(receiver)
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
        return "posts_$other"
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
                val isEdit = data.getBooleanExtra(PostCreateActivity.IS_EDIT, false)
                val position = data.getIntExtra(PostCreateActivity.POSITION, 0)
                if (isEdit) {
                    adapter.items[position] = item
                    adapter.notifyItemChanged(position)
                } else {
                    adapter.items.add(position, item)
                    adapter.notifyItemInserted(position)
                }
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

    override fun onEditClick(item: Post, adapterPosition: Int) {
        startActivityForResult(Intent(this, PostCreateActivity::class.java)
                .putExtra(PostCreateActivity.POST, item).putExtra(PostCreateActivity.POSITION, adapterPosition), SELECT_IMAGE)
    }
}
