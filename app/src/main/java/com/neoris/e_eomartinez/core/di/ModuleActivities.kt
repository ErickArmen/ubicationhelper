package com.neoris.e_eomartinez.core.di

import com.neoris.e_eomartinez.ubicationhelper.*
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