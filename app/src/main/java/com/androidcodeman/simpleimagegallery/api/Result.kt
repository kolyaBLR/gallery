package com.androidcodeman.simpleimagegallery.api

import com.google.gson.annotations.SerializedName

class Result {
    var status: String = ""
    var totalResults: Int = 0
    var articles = arrayListOf<Item>()
}