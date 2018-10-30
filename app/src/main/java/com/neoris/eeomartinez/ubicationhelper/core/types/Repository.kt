package com.neoris.eeomartinez.ubicationhelper.core.types

import io.reactivex.Completable

interface Repository <G, S> {
    fun getData(param: String = ""): G? = null
    fun setData(param: S): Completable = Completable.complete()
}