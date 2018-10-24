package com.neoris.e_eomartinez.ubicationhelper.core.types

import io.reactivex.Completable

interface Repository <G, S> {
    fun getData(param: String = ""): G? = null
    fun setData(param: S): Completable = Completable.complete()
}