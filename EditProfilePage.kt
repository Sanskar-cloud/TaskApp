package com.shaluambasta.phonenumlogin

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class EditProfilePage : AppCompatActivity(),OnMapReadyCallback {

    private lateinit var mailTextView: TextView
    private lateinit var nameTextView: TextView
    private lateinit var ageTextView: TextView
    private lateinit var collegeTextView: TextView
    // --Commented out by Inspection (14-09-2023 18:03):private lateinit var databaseReference: DatabaseReference
    private lateinit var mMap: GoogleMap

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val locationPermissionCode = 101

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile_page)

        mailTextView = findViewById(R.id.mail)
        nameTextView = findViewById(R.id.name)
        ageTextView = findViewById(R.id.age)
        collegeTextView = findViewById(R.id.college)

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)


        // Initialize Firebase Realtime Database reference



        val currentUser = FirebaseAuth.getInstance().currentUser

        if (currentUser != null) {
            val uid = currentUser.uid

            val userRef = FirebaseDatabase.getInstance().getReference("users")
            userRef.child(uid).addListenerForSingleValueEvent(object : ValueEventListener {

                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val user = dataSnapshot.getValue(User::class.java)
                    if (user != null) {

                        nameTextView.text="Name: ${user.name ?: "N/A"}"
                        mailTextView.text="Age: ${user.email ?: "N/A"}"
                        ageTextView.text="College: ${user.age ?: "N/A"}"
                        collegeTextView.text="Email: ${user.college ?: "N/A"}"
                    }





                        // Use the fetched data as needed

                    }


                override fun onCancelled(databaseError: DatabaseError) {
                    // Handle any errors here
                    val errorCode: Int = databaseError.code
                    val errorMessage = databaseError.message
                    Toast.makeText(this@EditProfilePage, errorMessage, Toast.LENGTH_SHORT).show()
                }
            })
        }


        // Attach a ValueEventListener to retrieve the data

    }
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Check and request location permission
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED) {
            // You can use the API that requires the permission.
            mMap.isMyLocationEnabled = true
            getCurrentLocation()
        } else {
            // You can directly ask for the permission.
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                locationPermissionCode
            )
        }
    }

    private fun getCurrentLocation() {
        fusedLocationClient.lastLocation.addOnSuccessListener(this) { location ->
            if (location != null) {
                val currentLatLng = LatLng(location.latitude, location.longitude)
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 15f))
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == locationPermissionCode) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, enable location features
                mMap.isMyLocationEnabled = true
                getCurrentLocation()
            }
        }
    }
}