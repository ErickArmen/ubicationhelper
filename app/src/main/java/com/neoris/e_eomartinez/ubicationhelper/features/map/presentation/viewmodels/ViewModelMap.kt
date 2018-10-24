package com.neoris.e_eomartinez.ubicationhelper.features.map.presentation.viewmodels

import android.arch.lifecycle.ViewModel
import android.util.Log
import com.neoris.e_eomartinez.ubicationhelper.core.types.UseCase.None
import com.neoris.e_eomartinez.ubicationhelper.features.map.domain.usecases.GetKeys
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

class ViewModelMap @Inject constructor(private val getKeys: GetKeys): ViewModel() {

    var keys: PublishSubject<String> = PublishSubject.create()

    fun loadDriverKeys() =
        getKeys(None()){ obs ->
            obs.subscribe {
                keys.onNext(it)
            }
        }
}