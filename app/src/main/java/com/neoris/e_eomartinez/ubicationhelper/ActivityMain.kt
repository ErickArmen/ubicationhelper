package com.neoris.e_eomartinez.ubicationhelper

import android.content.Intent
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.widget.SearchView
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.google.android.gms.maps.*

import android.view.MenuInflater
import android.R.string.cancel
import android.content.DialogInterface
import android.text.InputType
import android.support.v4.widget.SearchViewCompat.setInputType
import android.support.v7.app.AlertDialog
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.android.gms.maps.model.*
import com.google.maps.android.PolyUtil

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint
import com.google.firebase.firestore.QueryDocumentSnapshot
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class ActivityMain : AppCompatActivity(), OnMapReadyCallback,
        NavigationView.OnNavigationItemSelectedListener, View.OnClickListener,
        GoogleMap.OnMapClickListener, GoogleMap.OnPolygonClickListener,
        MapController.MapControllerCallback {


    private lateinit var mMap: GoogleMap
    private val firestore = FirebaseFirestore.getInstance()
    private val polyLine = PolylineOptions().width(4f).color(Color.BLUE)
    private var line: Polyline? = null
    private lateinit var mMapCenterLatLng: LatLng
    private lateinit var mController: MapController
    private var mDefaultZoom = 13f
    private var mLstMarkersPlace : ArrayList<Marker> = ArrayList()
    private var mMarkerPostalCode: Marker? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        mainNaviView.itemIconTintList = null
        mainNaviView.setNavigationItemSelectedListener(this)
        btn_show_last_route.setOnClickListener(this)
        initVars()
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        initListeners()
        writeInFireB(googleMap)

        /*val neoris = LatLng(25.699820, -100.261592)
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(25.699507, -100.259), 16f))
        mMap.addMarker(MarkerOptions().position(neoris).title("Bus Station").icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_bus)))
        mMap.addMarker(MarkerOptions().position(LatLng(25.698283, -100.260168)).title("Coffee Shop").icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_coffee)))
        mMap.addMarker(MarkerOptions().position(LatLng(25.700195, -100.256913)).title("Car Wash").icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_car)))
        mMap.addMarker(MarkerOptions().position(LatLng(25.698874, -100.257411)).title("Church").icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_church)))
        mMap.addMarker(MarkerOptions().position(LatLng(25.700680, -100.259364)).title("Park").icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_park)))
        mMap.addMarker(MarkerOptions().position(LatLng(25.701658, -100.258504)).title("Restaurant").icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_restaurant)))
        mMap.addMarker(MarkerOptions().position(LatLng(25.699507, -100.259549)).title("Shop").icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_shop)))*/

    }

    fun initVars() {
        this.mMapCenterLatLng = LatLng(25.675437, -100.416310)
        this.mController = MapController(this)
    }

    fun initListeners(){
        this@ActivityMain.mMap.moveCamera(CameraUpdateFactory
                .newLatLngZoom(this@ActivityMain.mMapCenterLatLng,
                        this@ActivityMain.mDefaultZoom))
        this@ActivityMain.mMap.setOnMapClickListener(this@ActivityMain)
        this@ActivityMain.mMap.setOnPolygonClickListener(this@ActivityMain)
        this@ActivityMain.mController.getZones()
    }

    private fun goToTimeLine(title: String){
        val intent = Intent(this, ActivityTimeLine::class.java)
        intent.putExtra("title", title)
        startActivity(intent)
    }

    private fun writeInFireB(googleMap: GoogleMap){

        val database = FirebaseDatabase.getInstance().getReference()
        val marker1 = googleMap.addMarker(MarkerOptions().position(LatLng(0.0, 0.0))
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_car_tracking)))
        val marker2 = googleMap.addMarker(MarkerOptions().position(LatLng(0.0, 0.0))
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_car_tracking)))

        database.child("driver_1").addValueEventListener(object: ValueEventListener{
            override fun onDataChange(p0: DataSnapshot) {
                if (p0.getValue(Location::class.java) != null){
                    val location = p0.getValue(Location::class.java)!!
                    marker1.position = LatLng(location.lat, location.lon)
                }
            }
            override fun onCancelled(p0: DatabaseError) {}
        })

        database.child("driver_2").addValueEventListener(object: ValueEventListener{
            override fun onDataChange(p0: DataSnapshot) {
                if (p0.getValue(Location::class.java) != null){
                    val location = p0.getValue(Location::class.java)!!
                    marker2.position = LatLng(location.lat, location.lon)
                }

            }
            override fun onCancelled(p0: DatabaseError) {}
        })
    }

    private fun showLastRoute(){
        line?.remove()
        firestore.collection("locations").get().addOnCompleteListener {
            if (it.isSuccessful){
                val size = it.result.size()
                val document = it.result.documents[size-1]
                document.data?.values?.forEach {
                    val location = it as GeoPoint
                    polyLine.add(LatLng(location.latitude, location.longitude))
                }
                line = mMap.addPolyline(polyLine)
            }else{
                Log.i("===============Error", "Error getting documents" , it.exception)
            }
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.action_search ->{
                buildPostalCodeDialog()
            }
            R.id.journeys -> {
                goToTimeLine(item.title.toString())
            }
        }

        mainDrawer.closeDrawer(GravityCompat.START)
        return true
    }

    fun buildPostalCodeDialog(): Unit {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(resources.getString(R.string.search))
        val input = EditText(this)
        input.inputType = InputType.TYPE_CLASS_TEXT
        builder.setView(input)
        builder.setPositiveButton(resources.getString(android.R.string.ok),
                DialogInterface.OnClickListener {
                    dialog, which ->
                    mController.validatePostalCode(this, input.text.toString(), mMap)
                })
        builder.setNegativeButton(resources.getString(android.R.string.cancel),
                DialogInterface.OnClickListener {
                    dialog, which -> dialog.cancel()
                })

        builder.show()
    }

    override fun onMapClick(p0: LatLng?) {
        Log.d("position", "points.add(Models.Point("  + p0?.latitude.toString() + ", " + p0?.longitude.toString() + "))")
    }

    override fun onPolygonClick(polygon: Polygon) {
        this.mController.updateIndex(polygon)
    }

    override fun onGetZones(lstZoneModels: ArrayList<Models.Zone>) {
        for (zone in lstZoneModels) {
            zone.getPolygon(this.mMap)
        }
    }

    override fun onCurrentZoneSelected(zoneModel: Models.Zone) {
        for (marker in this.mLstMarkersPlace){
            marker.remove();
        }
        this.mLstMarkersPlace.clear()
        for(place in zoneModel.places) {
            this.mLstMarkersPlace.add(
                    mMap.addMarker(MarkerOptions().position(
                            LatLng(place.latitude, place.longitude)).title(place.title)
                            .icon(BitmapDescriptorFactory.fromResource(place.iconResource))))

        }
    }

    override fun onPostalCodeValidationResponse(postalCode: String, position: LatLng, isInZone: Boolean,
                                                zone: Models.Zone?) {
        try {
            this.mMarkerPostalCode?.remove()
            this.mMarkerPostalCode = mMap.addMarker(MarkerOptions().position(position).title(postalCode))
            if (isInZone)
                buildPostalCodeDetailDialog(postalCode + " pertenece a la zona: " + zone?.name)
            else
                buildPostalCodeDetailDialog("No pertenece a ninguna zona")
        }catch (ex: Exception){
            ex.toString()
        }
    }

    private fun buildPostalCodeDetailDialog(detail: String): Unit{
        val builder = AlertDialog.Builder(this)
        builder.setTitle(resources.getString(R.string.search))
        val input = TextView(this)
        input.text = detail
        builder.setView(input)
        builder.setPositiveButton(resources.getString(android.R.string.ok),
                DialogInterface.OnClickListener {
                    dialog, which ->
                    dialog.dismiss()
                })
        builder.setNegativeButton(resources.getString(android.R.string.cancel),
                DialogInterface.OnClickListener {
                    dialog, which -> dialog.cancel()
                })
        builder.show()
    }
}
