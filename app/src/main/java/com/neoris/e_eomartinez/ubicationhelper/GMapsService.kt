package com.neoris.e_eomartinez.ubicationhelper

import com.google.gson.Gson
import com.google.gson.JsonObject
import org.json.JSONObject
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GMapsService {
    @GET("maps/api/geocode/json")
    fun requestAddress(@Query("address") postalCode: String): Call<JsonObject>
}