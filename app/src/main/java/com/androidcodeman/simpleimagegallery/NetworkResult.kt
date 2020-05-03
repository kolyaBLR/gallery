package com.androidcodeman.simpleimagegallery

import com.androidcodeman.simpleimagegallery.json.Post
import java.io.Serializable

data class NetworkResult(val items: List<Post>): Serializable