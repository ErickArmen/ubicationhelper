package com.neoris.e_eomartinez.ubicationhelper.features.map.presentation.activities

import android.app.Activity
import android.arch.lifecycle.ViewModelProvider
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.util.Log
import android.view.MenuItem
import android.view.View

import android.support.constraint.ConstraintLayout
import android.support.design.widget.BottomSheetBehavior
import android.text.InputType
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
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
import com.neoris.e_eomartinez.ubicationhelper.*
import com.neoris.e_eomartinez.ubicationhelper.R
import com.neoris.e_eomartinez.ubicationhelper.core.extensions.observe
import com.neoris.e_eomartinez.ubicationhelper.core.extensions.toast
import com.neoris.e_eomartinez.ubicationhelper.core.extensions.viewModel
import com.neoris.e_eomartinez.ubicationhelper.features.journeys.presentation.activities.ActivityTimeLine
import com.neoris.e_eomartinez.ubicationhelper.features.login.presentation.ActivitySignIn
import com.neoris.e_eomartinez.ubicationhelper.features.map.domain.models.Location
import com.neoris.e_eomartinez.ubicationhelper.features.map.presentation.MapController
import com.neoris.e_eomartinez.ubicationhelper.features.map.presentation.MarkerAnimator
import com.neoris.e_eomartinez.ubicationhelper.features.map.presentation.Models
import com.neoris.e_eomartinez.ubicationhelper.features.map.presentation.viewmodels.ViewModelMap
import com.neoris.e_eomartinez.ubicationhelper.features.middleware.presentation.ActivityMiddleWare
import com.neoris.e_eomartinez.ubicationhelper.features.travels.presentation.activities.ActivityTravels
import dagger.android.AndroidInjection
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_main.*
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import kotlin.collections.HashMap
import kotlin.math.roundToInt

private const val requestMainToPrepare = 901

class ActivityMain : AppCompatActivity(), OnMapReadyCallback,
        NavigationView.OnNavigationItemSelectedListener, View.OnClickListener,
        GoogleMap.OnMapClickListener, GoogleMap.OnPolygonClickListener,
        MapController.MapControllerCallback {

    @Inject lateinit var vmFactory: ViewModelProvider.Factory
    private lateinit var viewModelMap: ViewModelMap
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
    private val disposable = CompositeDisposable()
    private var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>? = null
    //private lateinit var bubbleFactory: IconGenerator
    //private lateinit var myView: View


    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottomSheetBehavior = BottomSheetBehavior.from(bottom_sheet)
        bottomSheetBehavior?.state = BottomSheetBehavior.STATE_HIDDEN

        if (!isUserLogged())
            goToLogin()

        viewModelMap = viewModel(vmFactory){
            observe(disposable, keys,
                    onNext = ::saveKeysOnMap,
                    onError = {toast(it.message, Toast.LENGTH_LONG)})
        }

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        mainNaviView.itemIconTintList = null
        mainNaviView.setNavigationItemSelectedListener(this)
        btn_show_last_route.setOnClickListener(this)
        btn_clear.setOnClickListener(this)
        btn_sheet_accept.setOnClickListener(this)
        btn_sheet_cancel.setOnClickListener(this)
        img_black_back_arrow.setOnClickListener(this)
        tv_elapsed_time.text = ""
        initVars()
        initPlaceTexts()
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable.clear()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        initListeners()
        viewModelMap.loadDriverKeys()
    }

    private fun initVars() {
        mMapCenterLatLng = LatLng(25.675437, -100.416310)
        mController = MapController(this)
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

        val map = HashMap<String, Marker>()
        Log.i("TESTING", keyMap.size.toString())
        keyMap.forEach {
            map[it.value] = mMap.addMarker(MarkerOptions().position(LatLng(0.0, 0.0))
                    .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_car_tracking)))
        }

        markerAnimator = MarkerAnimator(mMap)

        database.addChildEventListener(object: ChildEventListener {
            override fun onCancelled(p0: DatabaseError) {}
            override fun onChildMoved(p0: DataSnapshot, p1: String?) {}

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {
                val location = p0.getValue(Location::class.java)!!
                markerAnimator.animateMarker(map[p0.key], LatLng(location.lat, location.lon))
                map[p0.key]?.rotation = location.bearing
            }

            override fun onChildAdded(p0: DataSnapshot, p1: String?) {}
            override fun onChildRemoved(p0: DataSnapshot) {}

        })
    }

    private fun showLastRoute() {
        line?.remove()
        val polyLine = PolylineOptions().width(4f).color(Color.BLUE)

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
        listenFirebase()
        getZones()
    }

    private fun goToPrepare(){
        val intent = Intent(this, ActivityPrepare::class.java)
        startActivityForResult(intent, requestMainToPrepare)
        bottomSheetBehavior?.state = BottomSheetBehavior.STATE_HIDDEN
    }

    private fun showAutoCompleteFragments(){
        viewsVisibility(img_black_back_arrow, false)
        //viewsVisibility(img_ready, false)
        viewsVisibility(startPlaceFragment.view, true)
        viewsVisibility(endPlaceFragment.view, true)
        bottomSheetBehavior?.state = BottomSheetBehavior.STATE_HIDDEN
    }

    private fun saveKeysOnMap(key: String) {
        keyMap[key] = key
        listenFirebase()
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

    private fun initPlaceTexts() {

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

        /*myView.img_pin.setImageResource(R.mipmap.ic_marker_green)
        myView.tv_marker_text.text = startPlace.name*/
        markerStart = mMap
                .addMarker(MarkerOptions()
                        .position(startPlace.latLng)
                        .title(startPlace.name.toString())
                        .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_marker_green)))
                        //.icon(BitmapDescriptorFactory.fromBitmap(bubbleFactory.makeIcon())))

        /*myView.img_pin.setImageResource(R.mipmap.ic_marker_red)
        myView.tv_marker_text.text = endPlace.name*/
        markerEnd = mMap
                .addMarker(MarkerOptions()
                        .position(endPlace.latLng)
                        .title(endPlace.name.toString())
                        .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_marker_red)))
        markerEnd?.showInfoWindow()
                        //.icon(BitmapDescriptorFactory.fromBitmap(bubbleFactory.makeIcon())))

        val arrayResults = FloatArray(1)
        android.location.Location.distanceBetween(startPlace.latLng.latitude, startPlace.latLng.longitude,
                endPlace.latLng.latitude, endPlace.latLng.longitude, arrayResults)
        computeCorrectZoom(middlePointBetweenTwoPlaces(startPlace, endPlace), arrayResults[0])

        //mController.getDirections(startPlace.address.toString(), endPlace.address.toString())

        viewsVisibility(startPlaceFragment.view, false)
        viewsVisibility(endPlaceFragment.view, false)
        //viewsVisibility(img_ready, true)
        viewsVisibility(img_black_back_arrow, true)


        bottomSheetBehavior?.state = BottomSheetBehavior.STATE_COLLAPSED
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
                mController.fillZones(it.result.documents)
            }else{
                Log.i("===============Error", "Error getting documents" , it.exception)
            }
        }
    }

    override fun onClick(p0: View?) {
        when(p0?.id){
            R.id.btn_show_last_route -> showLastRoute()
            R.id.btn_clear -> clearRoute()
            R.id.btn_sheet_accept -> goToPrepare()
            R.id.btn_sheet_cancel -> showAutoCompleteFragments()
            //R.id.img_ready -> goToPrepare()
            R.id.img_black_back_arrow -> showAutoCompleteFragments()
        }
    }
}
