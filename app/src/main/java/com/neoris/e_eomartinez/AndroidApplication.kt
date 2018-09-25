package com.neoris.e_eomartinez

import android.app.Activity
import android.app.Application
import com.neoris.e_eomartinez.core.di.DaggerComponentApp
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import javax.inject.Inject

/*
class AndroidApplication: Application(), HasActivityInjector {

    @Inject
    lateinit var dispatchingActivityInjector : DispatchingAndroidInjector<Activity>

    override fun onCreate() {
        super.onCreate()
        DaggerComponentApp.create().inject(this)
    }

    override fun activityInjector(): AndroidInjector<Activity> = dispatchingActivityInjector

}*/
