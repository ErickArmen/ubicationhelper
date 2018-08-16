package com.neoris.e_eomartinez.ubicationhelper

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.util.Log
import android.view.MenuItem
import com.google.android.gms.maps.*

import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.Polygon
import kotlinx.android.synthetic.main.activity_main.*

class ActivityMain : AppCompatActivity(), OnMapReadyCallback,
        NavigationView.OnNavigationItemSelectedListener, GoogleMap.OnMapClickListener,
        GoogleMap.OnPolygonClickListener, MapController.MapControllerCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var mMapCenterLatLng: LatLng
    private lateinit var mController: MapController
    private var mDefaultZoom = 14f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        mainNaviView.itemIconTintList = null
        mainNaviView.setNavigationItemSelectedListener(this)
        val st = "";
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

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val x = item.order
        when(x){

            0 -> {
                goToTimeLine(item.title.toString())
            }
        }

        mainDrawer.closeDrawer(GravityCompat.START)
        return true
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
        for(place in zoneModel.places) {
            mMap.addMarker(MarkerOptions().position(
                    LatLng(place.latitude, place.longitude)).title(place.title)
                    .icon(BitmapDescriptorFactory.fromResource(place.iconResource)))

        }
    }
}
