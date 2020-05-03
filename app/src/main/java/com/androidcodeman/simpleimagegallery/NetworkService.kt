package com.androidcodeman.simpleimagegallery

import android.app.IntentService
import android.content.Intent
import com.androidcodeman.simpleimagegallery.api.RetrofitInstance
import com.androidcodeman.simpleimagegallery.json.Post
import java.lang.Exception

class NetworkService : IntentService(ACTION) {

    override fun onHandleIntent(intent: Intent?) {
        try {
            val result = RetrofitInstance.instance().getPosts().execute()
            if (result.isSuccessful) {
                result.body()?.articles?.map { item ->
                    Post().apply {
                        this.title = item.title
                        this.description = item.description
                        this.imageUrl = item.urlToImage
                        this.isLocal = false
                    }
                }?.let {
                    sendBroadcast(Intent(ACTION).putExtra(RESULT, NetworkResult(it)))
                }
            } else {
                sendBroadcast(Intent(ACTION).putExtra(ERROR, "error"))
            }
        } catch (ex: Exception) {
            sendBroadcast(Intent(ACTION).putExtra(ERROR, "error"))
        } finally {
            stopSelf()
        }
    }

    companion object {
        const val RESULT = "result"
        const val ERROR = "error"
        const val ACTION = "network_posts"
    }
}