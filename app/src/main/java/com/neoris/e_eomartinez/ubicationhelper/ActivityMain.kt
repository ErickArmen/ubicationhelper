package com.neoris.e_eomartinez.ubicationhelper

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.widget.SearchView
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.google.android.gms.maps.*

import kotlinx.android.synthetic.main.activity_main.*
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


class ActivityMain : AppCompatActivity(), OnMapReadyCallback,
        NavigationView.OnNavigationItemSelectedListener, GoogleMap.OnMapClickListener,
        GoogleMap.OnPolygonClickListener, MapController.MapControllerCallback {

    private lateinit var mMap: GoogleMap
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
