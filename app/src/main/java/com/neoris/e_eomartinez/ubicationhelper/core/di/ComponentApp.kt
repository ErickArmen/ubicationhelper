package com.neoris.e_eomartinez.ubicationhelper.core.di

import com.neoris.e_eomartinez.ubicationhelper.AndroidApplication
import dagger.Component
import dagger.android.AndroidInjectionModule

@Component(modules = [AndroidInjectionModule::class, ModuleApp::class, ModuleActivities::class])
interface ComponentApp {

    @Component.Builder
    interface Builder {
        fun build(): ComponentApp
    }

    fun inject (androidApp: AndroidApplication)
}