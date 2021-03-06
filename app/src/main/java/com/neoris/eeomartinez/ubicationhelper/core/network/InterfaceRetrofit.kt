package com.neoris.eeomartinez.ubicationhelper.core.network

import com.google.gson.JsonObject
import com.neoris.eeomartinez.ubicationhelper.BuildConfig
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface InterfaceRetrofit {

    @GET("maps/api/geocode/json")
    fun requestAddress(@Query("address") postalCode: String): Call<JsonObject>

    @GET("maps/api/directions/json")
    fun requestDirections(@Query("origin") origin: String, @Query("destination") destination: String,
                          @Query("key") key: String = BuildConfig.API_KEY_DIRECTIONS): Call<JsonObject>

}