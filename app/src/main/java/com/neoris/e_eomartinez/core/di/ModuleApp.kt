package com.neoris.e_eomartinez.core.di

import com.neoris.e_eomartinez.ubicationhelper.BuildConfig
import com.neoris.e_eomartinez.ubicationhelper.InterfaceRetrofit
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton


@Module
class ModuleApp {

    @Singleton
    @Provides
    @Named("GeoCode")
    fun injectRetrofitGeoCode(): InterfaceRetrofit =
        Retrofit.Builder()
                .baseUrl(BuildConfig.geocode)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(InterfaceRetrofit::class.java)

    @Singleton
    @Provides
    @Named("Directions")
    fun injectRetrofitDirections(): InterfaceRetrofit =
        Retrofit.Builder()
                .baseUrl(BuildConfig.directions)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(InterfaceRetrofit::class.java)






}

