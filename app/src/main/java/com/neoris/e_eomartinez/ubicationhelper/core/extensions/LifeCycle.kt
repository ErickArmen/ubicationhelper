package com.neoris.e_eomartinez.ubicationhelper.core.extensions

import android.arch.lifecycle.LifecycleOwner
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable

fun <T : Any, O : Observable<T>> LifecycleOwner.observe(disposable: CompositeDisposable,
                                                        observable: O, onNext: (T) -> Unit,
                                                        onError: (t: Throwable) -> Unit) =
        disposable.add(observable.subscribe(
                {
                    onNext(it)
                },
                {
                    onError(it)
                }
        ))