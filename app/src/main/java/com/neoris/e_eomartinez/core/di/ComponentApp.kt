package com.neoris.e_eomartinez.core.di

//import com.neoris.e_eomartinez.AndroidApplication
import dagger.Component
import dagger.android.AndroidInjectionModule

@Component(modules = arrayOf(AndroidInjectionModule::class, ModuleApp::class, ModuleActivities::class))
interface ComponentApp {

    @Component.Builder
    interface Builder{
        fun build(): ComponentApp
    }

    //fun inject (androidApp: AndroidApplication)
}