package com.neoris.e_eomartinez.ubicationhelper.features.map.presentation.viewmodels

import com.neoris.e_eomartinez.ubicationhelper.core.types.UseCase
import com.neoris.e_eomartinez.ubicationhelper.features.map.domain.usecases.GetKeys
import io.reactivex.Observable
import java.util.*
import javax.inject.Inject

class ViewModelMap @Inject constructor(private val getKeys: GetKeys) {

    fun getDriversKeys(): Observable<String> = getKeys.getKeys()

}