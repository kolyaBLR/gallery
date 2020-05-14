package com.androidcodeman.simpleimagegallery

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.androidcodeman.simpleimagegallery.fragments.map.WorkaroundMapFragment
import com.androidcodeman.simpleimagegallery.json.Post
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_detail.*
import java.io.File
import kotlin.math.roundToInt


class DetailActivity : MapActivity() {

    private lateinit var data: Post

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        image.layoutParams = image.layoutParams.apply {
            val size = intent.getIntExtra("width", 0)
            this.width = size
            this.height = (size.div(16.0) * 9.0).roundToInt()
        }

        data = intent.getSerializableExtra("data") as Post

        if (data.imageUrl != null && data.imageUrl != "") {
            image.visibility = View.VISIBLE
            if (data.isLocal) {
                Picasso.get().load(File(data.imageUrl)).into(image)
            } else {
                Picasso.get().load(data.imageUrl).into(image)
            }
        } else {
            image.visibility = View.GONE
        }
        titleItem.text = data.title
        descriptionItem.text = data.description

        initMapView(data.position != null)
    }

    override fun onMapReady(map: GoogleMap?) {
        super.onMapReady(map)
        data.position?.let { position ->
            val marker = MarkerOptions()
                    .title(data.title)
                    .snippet(data.description)
                    .position(position)
            map?.addMarker(marker)
            showPosition(position)
        }
    }
}
