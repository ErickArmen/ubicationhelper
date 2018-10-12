package com.neoris.e_eomartinez.ubicationhelper.core.extensions

import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

fun Completable.subsAndObsOnMain(): Completable =
        this.subscribeOn(Schedulers.computation()).observeOn(AndroidSchedulers.mainThread())