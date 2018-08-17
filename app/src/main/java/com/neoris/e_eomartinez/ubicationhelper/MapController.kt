package com.neoris.e_eomartinez.ubicationhelper

import android.content.Context
import android.widget.Toast
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Polygon
import com.google.gson.JsonObject
import com.google.maps.android.PolyUtil
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET




class MapController {
    private var mZIndex: Int = 0
    private lateinit var mLstZoneModels: ArrayList<Models.Zone>
    private lateinit var mCurrentZoneSelected: Models.Zone
    private lateinit var mControllerCallback: MapControllerCallback
    private lateinit var mRetrofit: Retrofit;

    constructor(controllerCallback: MapControllerCallback) {
        this.mControllerCallback = controllerCallback
        this.mRetrofit = Retrofit.Builder().baseUrl("http://maps.googleapis.com/")
                .addConverterFactory(GsonConverterFactory.create()).build()
    }

    fun getZones() {
        this.mLstZoneModels = DummyData().zones()
        this.mZIndex =  mLstZoneModels.size
        this.mControllerCallback.onGetZones(mLstZoneModels)
    }

    fun upZIndex(): Number {
        this.mZIndex +=1
        return this.mZIndex
    }

    fun updateIndex(polygon: Polygon) {
        polygon.setZIndex(upZIndex().toFloat())
        for (model in this.mLstZoneModels) {
            if (model.getPolygon(null)?.id.equals(polygon.getId(), true)) {
                this.mCurrentZoneSelected = model
                this.mControllerCallback.onCurrentZoneSelected(this.mCurrentZoneSelected)
                break
            }
        }
    }

    var count = 0;
    fun validatePostalCode(context: Context,postalCode: String,googleMap: GoogleMap ) {
        count += 1
        val service = this.mRetrofit.create<GMapsService>(GMapsService::class.java)
        val fullPostalCode = context.resources.getString(R.string.postal_code,postalCode, "");
        service.requestAddress(fullPostalCode)
                .enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                try {
                    val jsonResponse = response.body()?.asJsonObject?.get("results")
                    val jsonLocation = jsonResponse?.asJsonArray?.get(0)?.asJsonObject?.get("geometry")
                            ?.asJsonObject?.get("location")
                    var latitude = jsonLocation?.asJsonObject?.get("lat")?.asDouble as Double
                    var longitude = jsonLocation?.asJsonObject?.get("lng")?.asDouble as Double
                    var position = LatLng(latitude, longitude);
                    var isInZones = false;
                    var postalCodeZone: Models.Zone? = null
                    for (zone in mLstZoneModels) {
                        isInZones = PolyUtil.containsLocation(
                                position,zone.getPolygon(googleMap)?.points, isInZones)
                        if (isInZones){
                            postalCodeZone = zone;
                            break;
                        }
                    }
                    count = 0;
                    mControllerCallback.onPostalCodeValidationResponse(fullPostalCode, position,
                            isInZones, postalCodeZone)
                } catch (ex: Exception) {
                    if (count <= 3)
                        validatePostalCode(context,postalCode,googleMap )
                    else {
                        count = 0
                        Toast.makeText(context, context.resources.getString(R.string.no_result),
                                Toast.LENGTH_SHORT).show()
                    }
                }
            }
            override fun onFailure(call: Call<JsonObject>?, t: Throwable?) {}
        })
    }

    interface MapControllerCallback {
        fun onGetZones(lstZoneModels: ArrayList<Models.Zone>)
        fun onCurrentZoneSelected(zoneModel: Models.Zone)
        fun onPostalCodeValidationResponse(postalCode: String, position: LatLng, isInZone: Boolean, zone: Models.Zone?)
    }

}