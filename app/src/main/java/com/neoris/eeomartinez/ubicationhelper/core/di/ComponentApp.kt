package com.neoris.eeomartinez.ubicationhelper.core.di

import com.neoris.eeomartinez.ubicationhelper.AndroidApplication
import dagger.Component
import dagger.android.AndroidInjectionModule
import javax.inject.Singleton

@Singleton
@Component(modules = [AndroidInjectionModule::class, ModuleApp::class, ModuleActivities::class])
interface ComponentApp {

    @Component.Builder
    interface Builder {
        fun build(): ComponentApp
    }

    fun inject (androidApp: AndroidApplication)
}