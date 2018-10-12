package com.neoris.e_eomartinez.ubicationhelper.core.di


import com.neoris.e_eomartinez.ubicationhelper.features.login.presentation.ActivitySignIn
import com.neoris.e_eomartinez.ubicationhelper.features.map.presentation.activities.ActivityMain
import com.neoris.e_eomartinez.ubicationhelper.features.middleware.presentation.ActivityMiddleWare
import com.neoris.e_eomartinez.ubicationhelper.features.travels.presentation.activities.ActivityKardex
import com.neoris.e_eomartinez.ubicationhelper.features.travels.presentation.activities.ActivityTravels
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