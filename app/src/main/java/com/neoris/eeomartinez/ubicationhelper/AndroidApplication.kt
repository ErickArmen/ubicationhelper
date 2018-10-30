package com.neoris.eeomartinez.ubicationhelper

import android.app.Activity
import android.content.Context
import android.support.multidex.MultiDex
import android.support.multidex.MultiDexApplication
import com.neoris.eeomartinez.ubicationhelper.core.di.DaggerComponentApp
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import javax.inject.Inject


class AndroidApplication: MultiDexApplication(), HasActivityInjector {

    @Inject
    lateinit var dispatchingActivityInjector : DispatchingAndroidInjector<Activity>

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    override fun onCreate() {
        super.onCreate()
        DaggerComponentApp.create().inject(this)
    }

    override fun activityInjector(): AndroidInjector<Activity> = dispatchingActivityInjector

}
