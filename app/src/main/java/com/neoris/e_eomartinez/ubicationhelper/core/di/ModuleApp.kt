package com.neoris.e_eomartinez.ubicationhelper.core.di

import com.neoris.e_eomartinez.ubicationhelper.BuildConfig
import com.neoris.e_eomartinez.ubicationhelper.core.di.viewmodel.ModuleViewModel
import com.neoris.e_eomartinez.ubicationhelper.core.network.InterfaceRetrofit
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton


@Module(includes = [ModuleViewModel::class])
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

    /*@Singleton
    @Provides
    @Named("Keys")
    fun injectRepositoryKeys(): Repository<String, String> = RepositoryCloudKeys()*/

    /*@Singleton
    @Provides
    @Named("MiddleWare")
    fun injectRepositoryMW(): RepositoryMiddleWare = RepositoryCloudMW()*/
}

