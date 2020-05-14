package com.androidcodeman.simpleimagegallery

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.androidcodeman.simpleimagegallery.fragments.map.WorkaroundMapFragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import kotlinx.android.synthetic.main.activity_post_creator.*

abstract class MapActivity : BaseActivity(), OnMapReadyCallback {

    private var map: GoogleMap? = null
    private val mapFragment = WorkaroundMapFragment()

    private fun enableCurrentLocation() {
        map?.isMyLocationEnabled = true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (isLocationEnabled()) {
            enableCurrentLocation()
        } else {
            requestPermissions()
        }
    }

    protected fun initMapView(isShow: Boolean) {
        if (isShow) {
            supportFragmentManager.beginTransaction()
                    .add(R.id.mapFrame, mapFragment)
                    .commit()
            mapFragment.getMapAsync(this)
            mapFrame.visibility = View.VISIBLE
        } else {
            mapFrame.visibility = View.GONE
        }
    }

    @SuppressLint("MissingPermission")
    protected fun showCurrentLocation() {
        (getSystemService(Context.LOCATION_SERVICE) as? LocationManager)?.let { locationManager ->
            if (isLocationEnabled()) {
                (locationManager.getLastKnownLocation("gps")
                        ?: locationManager.getLastKnownLocation("network"))?.let { location ->
                    val position = LatLng(location.latitude, location.longitude)
                    showPosition(position)
                }
            }
        }
    }

    protected fun showPosition(position: LatLng) {
        map?.animateCamera(CameraUpdateFactory.newLatLngZoom(position, 13f))
        val cameraPosition = CameraPosition.Builder()
                .target(position)
                .zoom(14f)
                .build()
        map?.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
    }

    private fun isLocationEnabled() = ContextCompat.checkSelfPermission(this,
            Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED

    private fun requestPermissions() = ActivityCompat.requestPermissions(this,
            arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_REQUEST)

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_REQUEST && isLocationEnabled()) {
            enableCurrentLocation()
        }
    }

    override fun onMapReady(map: GoogleMap?) {
        this.map = map
        map?.let {
            if (isLocationEnabled()) {
                enableCurrentLocation()
            }
            mapFragment.setListener {
                ((findViewById<View>(android.R.id.content) as ViewGroup)
                        .getChildAt(0) as ViewGroup)
                        .requestDisallowInterceptTouchEvent(true)
            }
        }
    }

    companion object {
        private const val LOCATION_REQUEST = 213
    }
}