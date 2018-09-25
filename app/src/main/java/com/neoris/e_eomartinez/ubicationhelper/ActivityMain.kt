package com.neoris.e_eomartinez.ubicationhelper

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.util.Log
import android.view.MenuItem
import android.view.View

import android.graphics.drawable.ColorDrawable
import android.os.Handler
import android.text.InputType
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.firebase.ui.auth.AuthUI
import com.google.android.gms.common.api.Status
import com.google.android.gms.location.places.AutocompleteFilter
import com.google.android.gms.location.places.Place
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment
import com.google.android.gms.location.places.ui.PlaceSelectionListener
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.firestore.*
import com.google.maps.android.ui.IconGenerator
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.custom_marker_2.view.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap
import kotlin.math.roundToInt

private const val requestMainToPrepare = 901

class ActivityMain : AppCompatActivity(), OnMapReadyCallback,
        NavigationView.OnNavigationItemSelectedListener, View.OnClickListener,
        GoogleMap.OnMapClickListener, GoogleMap.OnPolygonClickListener,
        MapController.MapControllerCallback {

    private lateinit var mMap: GoogleMap
    private val firestore = FirebaseFirestore.getInstance()
    private var line: Polyline? = null
    private lateinit var mMapCenterLatLng: LatLng
    private lateinit var mController: MapController
    private var mDefaultZoom = 13f
    private val database = FirebaseDatabase.getInstance().reference
    private var mLstMarkersPlace : ArrayList<Marker> = ArrayList()
    private var mMarkerPostalCode: Marker? = null
    private var keyMap = HashMap<String, String>()
    private lateinit var markerAnimator: MarkerAnimator
    private lateinit var startPlaceFragment: PlaceAutocompleteFragment
    private lateinit var endPlaceFragment: PlaceAutocompleteFragment
    private var placeStart: Place? = null
    private var placeEnd: Place? = null
    private var markerStart: Marker? = null
    private var markerEnd: Marker? = null
    private lateinit var bubbleFactory: IconGenerator
    private lateinit var myView: View


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (!isUserLogged())
            goToLogin()

        getDriversKeys()

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        mainNaviView.itemIconTintList = null
        mainNaviView.setNavigationItemSelectedListener(this)
        btn_show_last_route.setOnClickListener(this)
        btn_clear.setOnClickListener(this)
        img_ready.setOnClickListener(this)
        img_black_back_arrow.setOnClickListener(this)
        setBubbleFactory()
        tv_elapsed_time.text = ""
        initVars()
        initPlaceTexts()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        initListeners()
        Handler().postDelayed({listenFirebase()}, 3000)
    }

    private fun initVars() {
        this.mMapCenterLatLng = LatLng(25.675437, -100.416310)
        this.mController = MapController(this)
    }

    private fun initListeners() {
        this@ActivityMain.mMap.moveCamera(CameraUpdateFactory
                .newLatLngZoom(this@ActivityMain.mMapCenterLatLng,
                        this@ActivityMain.mDefaultZoom))
        this@ActivityMain.mMap.setOnMapClickListener(this@ActivityMain)
        this@ActivityMain.mMap.setOnPolygonClickListener(this@ActivityMain)
//        getZones()
        firestore.collection("Zones")
                .addSnapshotListener { _, _ ->
                    mController.clearMap(mMap)
                    getZones()
                }
    }

    private fun goToTimeLine(title: String){
        val intent = Intent(this, ActivityTimeLine::class.java)
        intent.putExtra("title", title)
        startActivity(intent)
    }

    private fun listenFirebase() {

        /*val marker1 = googleMap.addMarker(MarkerOptions().position(LatLng(0.0, 0.0))
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_car_tracking)))
        val marker2 = googleMap.addMarker(MarkerOptions().position(LatLng(0.0, 0.0))
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_car_tracking)))*/

        val map = HashMap<String, Marker>()
        Log.i("TESTING", keyMap.size.toString())
        keyMap.forEach {
            map[it.value] = mMap.addMarker(MarkerOptions().position(LatLng(0.0, 0.0))
                    .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_car_tracking)))
        }

        /*for (i in 0..keyMapSize){
            map.put(keyMap[i], googleMap.addMarker(MarkerOptions().position(LatLng(0.0, 0.0))
                    .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_car_tracking))))
        }*/

        markerAnimator = MarkerAnimator(mMap)

        database.addChildEventListener(object: ChildEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {
            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {
                val location = p0.getValue(Location::class.java)!!
                //map[p0.key]?.position = LatLng(location.lat, location.lon)
                markerAnimator.animateMarker(map[p0.key], LatLng(location.lat, location.lon))
                map[p0.key]?.rotation = location.bearing
            }

            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
            }

            override fun onChildRemoved(p0: DataSnapshot) {
            }

        })

        /*keyMap.values.forEach {
            database.child()
        }

        database.child(keyMap[])*/




        /*database.child("driver_1").addValueEventListener(object: ValueEventListener{
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
        })*/
    }

    private fun showLastRoute() {
        line?.remove()
        val polyLine = PolylineOptions().width(4f).color(Color.BLUE)

        /*firestore.collection("locations").document("99").addSnapshotListener { documentSnapshot, firebaseFirestoreException ->
            if (documentSnapshot != null){
                val document = documentSnapshot
                val dateInit = document.data?.keys?.min()?.toLong()
                val dateLast = document.data?.keys?.max()?.toLong()
                val diffDate = dateLast?.minus(dateInit!!)
                Log.i("TESTING", "$dateInit $dateLast $diffDate")
                val sdf = SimpleDateFormat("mm:ss", Locale.getDefault())
                tv_elapsed_time.setText(getString(R.string.elapsed_time, sdf.format(diffDate)))

                val orderedMap = document.data?.toSortedMap()

                orderedMap?.values?.forEach {
                    val location = it as GeoPoint
                    polyLine.add(LatLng(location.latitude, location.longitude))
                }
                line = mMap.addPolyline(polyLine)
            }
        }*/


        firestore.collection("driver_1").get().addOnCompleteListener {
            if (it.isSuccessful){
                val size = it.result.size()
                val document = it.result.documents[size-1] as QueryDocumentSnapshot
                firestore.collection("driver_1").document(document.id).collection("locations")
                        .document("route").get().addOnCompleteListener {task ->
                            if (task.isSuccessful){

                                val dateInit = task.result.data?.keys?.min()?.toLong()
                                val dateLast = task.result.data?.keys?.max()?.toLong()
                                val diffDate = dateLast?.minus(dateInit!!)
                                val sdf = SimpleDateFormat("mm:ss", Locale.getDefault())
                                tv_elapsed_time.text = getString(R.string.elapsed_time, sdf.format(diffDate))

                                val orderedMap = task.result.data?.toSortedMap()

                                orderedMap?.values?.forEach {
                                    val location = it as GeoPoint
                                    polyLine.add(LatLng(location.latitude, location.longitude))
                                }
                                line = mMap.addPolyline(polyLine)
                            }
                        }
            }else{
                Log.i("===============Error", "Error getting documents" , it.exception)
            }
        }
    }

    private fun clearRoute() {
        line?.remove()
        tv_elapsed_time.text = ""
        mMap.clear()
        getZones()
        getDriversKeys()
        Handler().postDelayed({listenFirebase()}, 3000)
    }

    private fun goToPrepare(){
        val intent = Intent(this, ActivityPrepare::class.java)
        startActivityForResult(intent, requestMainToPrepare)
    }

    private fun showAutoCompleteFragments(){
        viewsVisibility(img_black_back_arrow, false)
        viewsVisibility(img_ready, false)
        viewsVisibility(startPlaceFragment.view, true)
        viewsVisibility(endPlaceFragment.view, true)
    }

    private fun getDriversKeys(){

        database.addListenerForSingleValueEvent(object: ValueEventListener{

            override fun onDataChange(p0: DataSnapshot) {

                p0.children.forEach{
                    keyMap[it.key!!] = it.key!!
                }

                /*p0.children.forEach {

                    database.child(it.key!!).addValueEventListener(object: ValueEventListener{
                        override fun onDataChange(p0: DataSnapshot) {
                            val location = p0.getValue(Location::class.java)!!
                            for (i in 0..100){
                                map.get(i.toString())?.position = LatLng(location.lat, location.lon)
                            }
                            *//*googleMap.addMarker(MarkerOptions().position(LatLng(location.lat, location.lon)).
                                    icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_car_tracking)))*//*
                        }

                        override fun onCancelled(p0: DatabaseError) {}
                    })
                }*/
            }

            override fun onCancelled(p0: DatabaseError) {}
        })
    }


    private fun isUserLogged(): Boolean = FirebaseAuth.getInstance().currentUser != null

    private fun goToLogin(){
        val intent = Intent(this, ActivitySignIn::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

    private fun logOut(){
        AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener {
                    goToLogin()
                }
    }

    private fun initPlaceTexts(){

        startPlaceFragment = fragmentManager.findFragmentById(R.id.place_fragment_start) as PlaceAutocompleteFragment
        endPlaceFragment = fragmentManager.findFragmentById(R.id.place_fragment_end) as PlaceAutocompleteFragment

        startPlaceFragment.setHint("Start")
        endPlaceFragment.setHint("End")

         val autocompleteFilter = AutocompleteFilter.Builder()
                .setTypeFilter(Place.TYPE_ADMINISTRATIVE_AREA_LEVEL_1)
                .setCountry("MX")
                .build()

        startPlaceFragment.setFilter(autocompleteFilter)
        endPlaceFragment.setFilter(autocompleteFilter)

        startPlaceFragment.setOnPlaceSelectedListener(object: PlaceSelectionListener{

            override fun onPlaceSelected(p0: Place?) {
                Log.i("Testing", "click Start")
                if (p0 != null && p0.latLng != null){
                    placeStart = p0
                }

                if (placeStart != null && placeEnd != null){
                    setTravelMarkers(placeStart!!, placeEnd!!)
                }
            }

            override fun onError(p0: Status?) {
                Toast.makeText(this@ActivityMain, p0?.statusMessage, Toast.LENGTH_SHORT).show()
            }
        })

        endPlaceFragment.setOnPlaceSelectedListener(object: PlaceSelectionListener{

            override fun onPlaceSelected(p0: Place?) {
                Log.i("Testing", "click End")
                if (p0 != null && p0.latLng != null){
                    placeEnd = p0
                }

                if (placeStart != null && placeEnd != null){
                    setTravelMarkers(placeStart!!, placeEnd!!)
                }
            }
            override fun onError(p0: Status?) {
                Toast.makeText(this@ActivityMain, p0?.statusMessage, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun setTravelMarkers(startPlace: Place, endPlace: Place) {

        if (markerStart != null && markerEnd != null){
            markerStart?.remove()
            markerEnd?.remove()
        }

        myView.img_pin.setImageResource(R.mipmap.ic_marker_green)
        myView.tv_marker_text.text = startPlace.name
        markerStart = mMap
                .addMarker(MarkerOptions()
                        .position(startPlace.latLng)
                        .icon(BitmapDescriptorFactory.fromBitmap(bubbleFactory.makeIcon())))

        myView.img_pin.setImageResource(R.mipmap.ic_marker_red)
        myView.tv_marker_text.text = endPlace.name
        markerEnd = mMap
                .addMarker(MarkerOptions()
                        .position(endPlace.latLng)
                        .icon(BitmapDescriptorFactory.fromBitmap(bubbleFactory.makeIcon())))

        val arrayResults = FloatArray(1)
        android.location.Location.distanceBetween(startPlace.latLng.latitude, startPlace.latLng.longitude,
                endPlace.latLng.latitude, endPlace.latLng.longitude, arrayResults)
        computeCorrectZoom(middlePointBetweenTwoPlaces(startPlace, endPlace), arrayResults[0])

        mController.getDirections(startPlace.address.toString(), endPlace.address.toString())

        viewsVisibility(startPlaceFragment.view, false)
        viewsVisibility(endPlaceFragment.view, false)
        viewsVisibility(img_ready, true)
        viewsVisibility(img_black_back_arrow, true)
    }

    private fun middlePointBetweenTwoPlaces(placeStart: Place, placeEnd: Place) =
        LatLng((placeStart.latLng.latitude + placeEnd.latLng.latitude) / 2,
                (placeStart.latLng.longitude + placeEnd.latLng.longitude) / 2)

    private fun computeCorrectZoom(middlePoint: LatLng, distance: Float) {

        when (distance.roundToInt()) {
            in 0..60 -> mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(middlePoint, 20f))
            in 60..255 -> mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(middlePoint, 18f))
            in 255..1000 -> mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(middlePoint, 16f))
            in 1000..4000 -> mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(middlePoint, 14f))
            in 4000..16500 -> mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(middlePoint, 12f))
            in 16500..65000 -> mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(middlePoint, 10f))
            in 65000..260000 -> mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(middlePoint, 8f))
            in 260000..1000000 -> mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(middlePoint, 6f))
            else -> mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(middlePoint, 4f))
        }
    }

    private fun viewsVisibility(view: View, visibility: Boolean){
        if (visibility){
            view.visibility = View.VISIBLE
            view.isClickable = true
        }else{
            view.visibility = View.INVISIBLE
            view.isClickable = false
        }
    }

    private fun setBubbleFactory(){
        bubbleFactory = IconGenerator(this)
        val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        myView = inflater.inflate(R.layout.custom_marker_2, null, false)
        bubbleFactory.setContentView(myView)
        bubbleFactory.setBackground(ColorDrawable(Color.TRANSPARENT))
    }

    private fun goToTravels(){
        startActivity(Intent(this, ActivityTravels::class.java))
    }

    private fun goToMiddleWare(){
        startActivity(Intent(this, ActivityMiddleWare::class.java))
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when(requestCode){
            requestMainToPrepare -> {
                when(resultCode){
                    Activity.RESULT_OK -> {
                        Toast.makeText(this@ActivityMain, "Your request is on the way", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.action_search -> {
                buildPostalCodeDialog()
            }
            R.id.journeys -> {
                goToTimeLine(item.title.toString())
            }
            R.id.Sign_out -> {
                logOut()
            }
            R.id.travels -> {
                goToTravels()
            }
            R.id.middleware ->{
                goToMiddleWare()
            }
        }

        mainDrawer.closeDrawer(GravityCompat.START)
        return true
    }

    private fun buildPostalCodeDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(resources.getString(R.string.search))
        val input = EditText(this)
        input.inputType = InputType.TYPE_CLASS_NUMBER
        builder.setView(input)
        builder.setPositiveButton(resources.getString(android.R.string.ok),
                {
                    _, _ ->
                    if (!input.text.isEmpty())
                        mController.validatePostalCode(this, input.text.toString(), mMap)
                    else
                        Toast.makeText(this@ActivityMain,
                                resources.getString(R.string.fill_input), Toast.LENGTH_SHORT).show()
                })
        builder.setNegativeButton(resources.getString(android.R.string.cancel),
                {
                    dialog, _ -> dialog.cancel()
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
        this.lbl_zone_selected.text = resources.getString(R.string.zone_selected,
                zoneModel.name)
        for (marker in this.mLstMarkersPlace){
            marker.remove()
        }
        this.mLstMarkersPlace.clear()
        for(place in zoneModel.places) {
            this.mLstMarkersPlace.add(
                    mMap.addMarker(MarkerOptions().position(
                            LatLng(place.latitude, place.longitude)).title(place.title)
                            .icon(BitmapDescriptorFactory.fromResource(place.iconResource))))

        }
        this.mMarkerPostalCode?.remove()
    }

    override fun onPostalCodeValidationResponse(postalCode: String, position: LatLng, isInZone: Boolean,
                                                zone: Models.Zone?) {
        try {
            if (isInZone) {
                buildPostalCodeDetailDialog(postalCode + " pertenece a la zona: " + zone?.name)
                onCurrentZoneSelected(zone!!)
                this.mMarkerPostalCode = mMap.addMarker(MarkerOptions().position(position).title(postalCode))
            } else {
                buildPostalCodeDetailDialog("No pertenece a ninguna zona")
//                this.lbl_zone_selected.text = resources.getString(R.string.no_zone_selected)
                this.mMarkerPostalCode?.remove()
            }
        }catch (ex: Exception){
            ex.toString()
        }
    }

    private fun buildPostalCodeDetailDialog(detail: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(resources.getString(R.string.search))
        val input = TextView(this)
        input.text = detail
        builder.setView(input)
        builder.setPositiveButton(resources.getString(android.R.string.ok),
                {
                    dialog, _ ->
                    dialog.dismiss()
                })
        builder.setNegativeButton(resources.getString(android.R.string.cancel),
                {
                    dialog, _ -> dialog.cancel()
                })
        builder.show()
    }

    private fun getZones(){
        firestore.collection("Zones").get().addOnCompleteListener {
            if (it.isSuccessful){
                this@ActivityMain.mController.fillZones(it.result.documents)
            }else{
                Log.i("===============Error", "Error getting documents" , it.exception)
            }
        }
    }

    override fun onClick(p0: View?) {
        when(p0?.id){
            R.id.btn_show_last_route -> showLastRoute()
            R.id.btn_clear -> clearRoute()
            R.id.img_ready -> goToPrepare()
            R.id.img_black_back_arrow -> showAutoCompleteFragments()
        }
    }
}
