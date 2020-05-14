package com.androidcodeman.simpleimagegallery.json

import com.google.android.gms.maps.model.LatLng
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

    var position: LatLng?
        get() {
            val lat = latitude ?: return null
            val lon = longitude ?: return null
            return LatLng(lat, lon)
        }
        set(value) {
            latitude = value?.latitude
            longitude = value?.longitude
        }

    private var latitude: Double? = null

    private var longitude: Double? = null
}