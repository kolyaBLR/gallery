package com.androidcodeman.simpleimagegallery.json

import com.google.gson.annotations.SerializedName

class JsonData(posts: ArrayList<Post>? = null) {

    @SerializedName("posts")
    var posts: ArrayList<Post>? = arrayListOf()

    init {
        this.posts = posts
    }
}