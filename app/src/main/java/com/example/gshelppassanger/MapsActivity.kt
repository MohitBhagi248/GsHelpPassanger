package com.example.gshelppassanger

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.maps.android.PolyUtil
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.example.gshelppassanger.databinding.ActivityMapsBinding

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Build
import android.util.Log
import android.widget.EditText
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.*
import com.google.android.gms.maps.model.*

import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.model.RectangularBounds
import com.google.android.libraries.places.api.model.TypeFilter
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest
import com.google.android.libraries.places.api.net.FindCurrentPlaceResponse
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import java.io.IOException


class MapsActivity : AppCompatActivity(), OnMapReadyCallback, LocationListener,
    GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    internal lateinit var mLastLocation: Location
    internal var mCurrLocationMarker: Marker? = null
    internal var mGoogleApiClient: GoogleApiClient? = null
    internal lateinit var mLocationRequest: LocationRequest
    private lateinit var polyline: Polyline

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }
    val shell = LatLng(43.77726, -79.34830)
    val esso = LatLng(43.77098, -79.37623)
    val petroCanada = LatLng(43.78981, -79.35325)
    val essoo = LatLng(43.77624, -79.32548)
    val esoo = LatLng(43.76403, -79.31808)
    val petrocanada = LatLng(43.77995, -79.30888)
    val shll = LatLng(43.75723, -79.35946)
    private var locationArrayList: ArrayList<LatLng>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        supportActionBar?.hide()

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.button.setOnClickListener {
//            searchLocation()
            location()
        }

//        val mapFragment = supportFragmentManager.findFragmentById(R.id.myMap) as SupportMapFragment
//        mapFragment.getMapAsync(this)
        val mapFragment =
            supportFragmentManager.findFragmentById(R.id.myMap) as SupportMapFragment
        mapFragment.getMapAsync { map ->
            mMap = map
            setupMap()
        }

        locationArrayList = ArrayList()
        locationArrayList!!.add(shell)
        locationArrayList!!.add(esso)
        locationArrayList!!.add(petroCanada)
        locationArrayList!!.add(essoo)
        locationArrayList!!.add(esoo)
        locationArrayList!!.add(petrocanada)
        locationArrayList!!.add(shll)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        }

    fun location(){
        for (i in locationArrayList!!.indices) {
            if (i == 0) {
                mMap.addMarker(MarkerOptions().position(locationArrayList!![i]).title("Shell"))
            } else if (i == 1) {
                mMap.addMarker(MarkerOptions().position(locationArrayList!![i]).title("ESSO"))
            } else if (i == 2) {
                mMap.addMarker(MarkerOptions().position(locationArrayList!![i]).title("ESSO"))
            } else if (i == 3) {
                mMap.addMarker(MarkerOptions().position(locationArrayList!![i]).title("Petro Canada"))
            } else if (i == 4) {
                mMap.addMarker(MarkerOptions().position(locationArrayList!![i]).title("Petro Canada"))
            } else if (i == 5) {
                mMap.addMarker(MarkerOptions().position(locationArrayList!![i]).title("Shell"))
            } else if (i == 6) {
                mMap.addMarker(MarkerOptions().position(locationArrayList!![i]).title("Shell"))
            }
            mMap.animateCamera(CameraUpdateFactory.zoomTo(15.0f))
            mMap.moveCamera(CameraUpdateFactory.newLatLng(locationArrayList!!.get(i)))
        }
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            if (ContextCompat.checkSelfPermission(
//                    this,
//                    Manifest.permission.ACCESS_FINE_LOCATION
//                ) == PackageManager.PERMISSION_GRANTED
//            ) {
//                buildGoogleApiClient()
//                mMap!!.isMyLocationEnabled = true
//            }
//        } else {
//            buildGoogleApiClient()
//            mMap!!.isMyLocationEnabled = true
//        }
    }

//    protected fun buildGoogleApiClient() {
//        mGoogleApiClient = GoogleApiClient.Builder(this)
//            .addConnectionCallbacks(this)
//            .addOnConnectionFailedListener(this)
//            .addApi(LocationServices.API).build()
//        mGoogleApiClient!!.connect()
//    }
//
    override fun onLocationChanged(location: Location) {
        mLastLocation = location
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker!!.remove()
        }

        val latLng = LatLng(location.latitude, location.longitude)
        val markerOptions = MarkerOptions()
        markerOptions.position(latLng)
        markerOptions.title("Current Position")
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
        mCurrLocationMarker = mMap!!.addMarker(markerOptions)

        mMap!!.moveCamera(CameraUpdateFactory.newLatLng(latLng))
        mMap!!.moveCamera(CameraUpdateFactory.zoomTo(11f))

        if (mGoogleApiClient != null) {
            LocationServices.getFusedLocationProviderClient(this)
        }
    }

    override fun onConnected(p0: Bundle?) {

        mLocationRequest = LocationRequest()
        mLocationRequest.interval = 1000
        mLocationRequest.fastestInterval = 1000
        mLocationRequest.priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            LocationServices.getFusedLocationProviderClient(this)
        }
    }

    override fun onConnectionSuspended(p0: Int) {

    }

    override fun onConnectionFailed(p0: ConnectionResult) {

    }
//
    private fun setupMap() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        } else {
            enableMyLocation()
        }
    }

    private fun enableMyLocation() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            mMap.isMyLocationEnabled = true

            // Get the current location and move the camera
            val fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                if (location != null) {
                    val currentLatLng = LatLng(location.latitude, location.longitude)
                    mMap.addMarker(
                        MarkerOptions().position(currentLatLng).title("Current Location")
                    )
                    mMap.moveCamera(
                        CameraUpdateFactory.newLatLngZoom(
                            currentLatLng,
                            15f
                        )
                    )
                }
            }
        }
    }
//
//    fun searchLocation() {
//        val locationSearch: EditText = findViewById(R.id.etSearch)
//        var location: String
//        location = locationSearch.text.toString().trim()
//        var addressList: List<Address>? = null
//
//        if (location == null || location == "") {
//            Toast.makeText(this, "provide location", Toast.LENGTH_SHORT).show()
//        } else {
//            val geoCoder = Geocoder(this)
//            try {
//                addressList = geoCoder.getFromLocationName(location, 1)
//            } catch (e: IOException) {
//                e.printStackTrace()
//            }
//
//            val address = addressList!![0]
//            val latLng = LatLng(address.latitude, address.longitude)
//            mMap!!.addMarker(MarkerOptions().position(latLng).title(location))
//            mMap!!.animateCamera(CameraUpdateFactory.newLatLng(latLng))
//        }
//    }
}