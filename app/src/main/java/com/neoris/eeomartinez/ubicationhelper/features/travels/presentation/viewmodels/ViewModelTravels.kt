package com.neoris.eeomartinez.ubicationhelper.features.travels.presentation.viewmodels

import android.arch.lifecycle.ViewModel
import android.arch.paging.PagedList
import android.arch.paging.RxPagedListBuilder
import com.neoris.eeomartinez.ubicationhelper.features.travels.presentation.recyclers.DataSourceFactory
import io.reactivex.Observable
import javax.inject.Inject

class ViewModelTravels @Inject constructor(private val dataSourceFactory: DataSourceFactory):ViewModel() {

    val travelList2: Observable<PagedList<String>>

    init {
        val config = PagedList.Config.Builder()
                .setPageSize(10)
                .setInitialLoadSizeHint(10)
                .setEnablePlaceholders(false)
                .build()

        travelList2 = RxPagedListBuilder<String, String>(dataSourceFactory, config)
                .setInitialLoadKey("0")
                .buildObservable()
    }

    fun onDestroy(){
        dataSourceFactory.onDestroy()
    }

}