package com.neoris.eeomartinez.ubicationhelper.core.di


import com.neoris.eeomartinez.ubicationhelper.features.login.presentation.ActivitySignIn
import com.neoris.eeomartinez.ubicationhelper.features.map.presentation.activities.ActivityMain
import com.neoris.eeomartinez.ubicationhelper.features.middleware.presentation.ActivityMiddleWare
import com.neoris.eeomartinez.ubicationhelper.features.travels.presentation.activities.ActivityKardex
import com.neoris.eeomartinez.ubicationhelper.features.travels.presentation.activities.ActivityTravels
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
interface ModuleActivities {

    @ContributesAndroidInjector
    fun contributeMain(): ActivityMain

    @ContributesAndroidInjector
    fun contributeSignIn(): ActivitySignIn

    @ContributesAndroidInjector
    fun contributeKardex(): ActivityKardex

    @ContributesAndroidInjector
    fun contributeTravels(): ActivityTravels

    @ContributesAndroidInjector
    fun contributeMiddleWare(): ActivityMiddleWare

}