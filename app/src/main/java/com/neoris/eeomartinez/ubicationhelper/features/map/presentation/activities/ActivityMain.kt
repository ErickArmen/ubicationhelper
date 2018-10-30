package com.neoris.eeomartinez.ubicationhelper.features.map.presentation.activities

import android.app.Activity
import android.arch.lifecycle.ViewModelProvider
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.view.MenuItem
import android.view.View

import android.support.constraint.ConstraintLayout
import android.support.design.widget.BottomSheetBehavior
import android.support.v7.app.AppCompatActivity
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
import com.google.firebase.database.DataSnapshot
import com.google.firebase.firestore.GeoPoint
import com.neoris.eeomartinez.ubicationhelper.R
import com.neoris.eeomartinez.ubicationhelper.core.extensions.*
import com.neoris.eeomartinez.ubicationhelper.features.login.presentation.ActivitySignIn
import com.neoris.eeomartinez.ubicationhelper.features.map.domain.models.Location
import com.neoris.eeomartinez.ubicationhelper.features.map.domain.models.RawRoute.Route
import com.neoris.eeomartinez.ubicationhelper.features.map.presentation.viewmodels.ViewModelMap
import com.neoris.eeomartinez.ubicationhelper.features.middleware.presentation.ActivityMiddleWare
import com.neoris.eeomartinez.ubicationhelper.features.prepare.ActivityPrepare
import com.neoris.eeomartinez.ubicationhelper.features.travels.presentation.activities.ActivityTravels
import dagger.android.AndroidInjection
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject
import kotlin.collections.HashMap


class ActivityMain : AppCompatActivity(), OnMapReadyCallback,
        NavigationView.OnNavigationItemSelectedListener, View.OnClickListener{

    companion object {
        private const val requestMainToPrepare = 901
        private const val driverId = "driver_1"
        private const val startHint = "Start"
        private const val endHint = "End"
        private const val countryMock = "MX"
    }

    @Inject lateinit var vmFactory: ViewModelProvider.Factory
    private lateinit var viewModelMap: ViewModelMap
    private lateinit var mMap: GoogleMap
    private var line: Polyline? = null
    private var mMapCenterLatLng = LatLng(25.6777588, -100.3152597)
    private var mDefaultZoom = 13f
    private var mapMarkers = HashMap<String, Marker>()
    private lateinit var startPlaceFragment: PlaceAutocompleteFragment
    private lateinit var endPlaceFragment: PlaceAutocompleteFragment
    private var placeStart: Place? = null
    private var placeEnd: Place? = null
    private var markerStart: Marker? = null
    private var markerEnd: Marker? = null
    private val disposable = CompositeDisposable()
    private var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (!isUserLogged())
            goToLogin()

        //Alternatively can create viewModelMap = viewModel(vmFactory) without lambda
        //then start observing until I need the actual values (onMapReady)
        //in that way i don't need to create keys(subjectPub) and call directly loadDriverKeys()
        viewModelMap = viewModel(vmFactory){
            observe(disposable, keys,
                    onNext = ::addMarkersToMap,
                    onError = ::showError)
            observe(disposable, locations,
                    onNext = ::receiveLocations,
                    onError = ::showError)
            observe(disposable, routes,
                    onNext = ::showLastRoute,
                    onError = ::showError)
        }
        initializeViews()
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable.clear()
    }

    private fun initializeViews(){
        bottomSheetBehavior =   BottomSheetBehavior.from(bottom_sheet)
        bottomSheetBehavior?.   state = BottomSheetBehavior.STATE_HIDDEN
        val mapFragment =       supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.            getMapAsync(this)
        mainNaviView.           itemIconTintList = null
        mainNaviView.           setNavigationItemSelectedListener(this)
        btn_show_last_route.    setOnClickListener(this)
        btn_clear.              setOnClickListener(this)
        btn_sheet_accept.       setOnClickListener(this)
        btn_sheet_cancel.       setOnClickListener(this)
        img_black_back_arrow.   setOnClickListener(this)
        tv_elapsed_time.        text = ""
        initPlaceTexts()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        googleMap.apply {
            mMap = this
            moveCamera(CameraUpdateFactory.newLatLngZoom(mMapCenterLatLng, mDefaultZoom))
        }
        viewModelMap.loadDriverKeys()
        viewModelMap.observeLocations()
    }

    private fun receiveLocations(snapshot: DataSnapshot) {
        mapMarkers[snapshot.key]?.animate(mMap, snapshot.getValue(Location::class.java))
    }

    private fun showLastRoute(route: Route) {
        line?.remove()
        val polyLine = PolylineOptions().width(4f).color(Color.BLUE)
        tv_elapsed_time.text = getString(R.string.elapsed_time, route.time)
        route.points.values.forEach {geopoint ->
            val location = geopoint as GeoPoint
            polyLine.add(LatLng(location.latitude, location.longitude))
        }
        line = mMap.addPolyline(polyLine)
    }

    private fun clearMap() {
        line?.remove()
        tv_elapsed_time.text = ""
    }

    private fun addMarkersToMap(markerId: String) {
        mapMarkers[markerId] = mMap.addMarker(MarkerOptions().position(LatLng(0.0, 0.0))
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_car_tracking)))
    }

    private fun showError(throwable: Throwable) = toast(throwable.message, Toast.LENGTH_LONG)

    private fun initPlaceTexts() {

        val autocompleteFilter = AutocompleteFilter.Builder()
                .setTypeFilter(Place.TYPE_ADMINISTRATIVE_AREA_LEVEL_1)
                .setCountry(countryMock)
                .build()

        startPlaceFragment = (fragmentManager.findFragmentById(R.id.place_fragment_start)
                as PlaceAutocompleteFragment).apply {
            setHint(startHint)
            setFilter(autocompleteFilter)
            setOnPlaceSelectedListener(createPlaceSelectionListener {
                placeStart = it
            })
        }
        endPlaceFragment = (fragmentManager.findFragmentById(R.id.place_fragment_end)
                as PlaceAutocompleteFragment).apply {
            setHint(endHint)
            setFilter(autocompleteFilter)
            setOnPlaceSelectedListener(createPlaceSelectionListener {
                placeEnd = it
            })
        }
    }

    private fun createPlaceSelectionListener(placeAssign: (place: Place)-> Unit): PlaceSelectionListener{

        return object: PlaceSelectionListener{

            override fun onPlaceSelected(p0: Place?) {
                if (p0 != null && p0.latLng != null){
                    placeAssign(p0)
                }

                if (placeStart != null && placeEnd != null){
                    setTravelMarkers(placeStart!!, placeEnd!!)
                    hideAutoCompleteFragments()
                }
            }

            override fun onError(p0: Status?) {
                Toast.makeText(this@ActivityMain, p0?.statusMessage, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showAutoCompleteFragments(){
        img_black_back_arrow.invisible()
        startPlaceFragment.view.visible()
        endPlaceFragment.view.visible()
        bottomSheetBehavior?.state = BottomSheetBehavior.STATE_HIDDEN
    }

    private fun hideAutoCompleteFragments(){
        startPlaceFragment.view.invisible()
        endPlaceFragment.view.invisible()
        img_black_back_arrow.visible()
        bottomSheetBehavior?.state = BottomSheetBehavior.STATE_COLLAPSED
    }

    private fun setTravelMarkers(startPlace: Place, endPlace: Place) {

        if (markerStart != null && markerEnd != null){
            markerStart?.remove()
            markerEnd?.remove()
        }

        markerStart = mMap
                .addMarker(MarkerOptions()
                        .position(startPlace.latLng)
                        .title(startPlace.name.toString())
                        .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_marker_green)))

        markerEnd = mMap
                .addMarker(MarkerOptions()
                        .position(endPlace.latLng)
                        .title(endPlace.name.toString())
                        .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_marker_red)))

        markerEnd?.showInfoWindow()
        startPlace.zoomBetweenPlaces(endPlace, mMap)
    }

    private fun isUserLogged(): Boolean = FirebaseAuth.getInstance().currentUser != null

    private fun logOut(){
        AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener {
                    goToLogin()
                }
    }

    private fun goToLogin(){
        val intent = Intent(this, ActivitySignIn::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

    private fun goToTravels(){
        startActivity(Intent(this, ActivityTravels::class.java))
    }

    private fun goToMiddleWare(){
        startActivity(Intent(this, ActivityMiddleWare::class.java))
    }

    private fun goToPrepare(){
        val intent = Intent(this, ActivityPrepare::class.java)
        startActivityForResult(intent, requestMainToPrepare)
        bottomSheetBehavior?.state = BottomSheetBehavior.STATE_HIDDEN
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when(requestCode){
            requestMainToPrepare -> {
                when(resultCode){
                    Activity.RESULT_OK -> {
                        Toast.makeText(this@ActivityMain, getString(R.string.request_on_way), Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
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

    override fun onClick(p0: View?) {
        when(p0?.id){
            R.id.btn_show_last_route -> viewModelMap.getRoutes(driverId)
            R.id.btn_clear -> clearMap()
            R.id.btn_sheet_accept -> goToPrepare()
            R.id.btn_sheet_cancel -> showAutoCompleteFragments()
            R.id.img_black_back_arrow -> showAutoCompleteFragments()
        }
    }
}
