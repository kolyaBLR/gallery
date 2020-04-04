package com.androidcodeman.simpleimagegallery.json

import com.google.gson.annotations.SerializedName

class JsonData(images: ArrayList<String>? = null) {

    @SerializedName("images")
    var images: ArrayList<String>? = arrayListOf()

    init {
        this.images = images
    }
}