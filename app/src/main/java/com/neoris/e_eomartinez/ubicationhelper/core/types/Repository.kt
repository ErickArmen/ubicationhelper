package com.neoris.e_eomartinez.ubicationhelper.core.types

import io.reactivex.Completable
import io.reactivex.Observable

interface Repository<G, S> {

    fun getData(param: String = ""): Observable<G>

    fun setData(param: S): Completable
}