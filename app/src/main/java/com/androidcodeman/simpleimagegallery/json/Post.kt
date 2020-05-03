package com.androidcodeman.simpleimagegallery.json

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class Post : Serializable {

    @SerializedName("url")
    var imageUrl: String? = null

    @SerializedName("title")
    var title = ""

    @SerializedName("description")
    var description = ""

    var isLocal = true
}