package com.neoris.eeomartinez.ubicationhelper.features.travels.presentation.recyclers

import android.arch.paging.DataSource
import javax.inject.Inject

class DataSourceFactory @Inject constructor(private val dataSource: DataSourceTravels):
        DataSource.Factory<String, String>() {

    override fun create(): DataSource<String, String> {
        return dataSource
    }

    fun onDestroy(){
        dataSource.onDestroy()
    }


}