package com.neoris.eeomartinez.ubicationhelper.core.di

import com.neoris.eeomartinez.ubicationhelper.BuildConfig
import com.neoris.eeomartinez.ubicationhelper.core.di.viewmodel.ModuleViewModel
import com.neoris.eeomartinez.ubicationhelper.core.network.InterfaceRetrofit
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

//Example Module app
@Module(includes = [ModuleViewModel::class])
class ModuleApp {

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

