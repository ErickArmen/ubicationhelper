package com.neoris.eeomartinez.ubicationhelper.features.map.presentation.viewmodels

import android.arch.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.neoris.eeomartinez.ubicationhelper.core.types.UseCase.None
import com.neoris.eeomartinez.ubicationhelper.features.map.domain.models.RawRoute.Route
import com.neoris.eeomartinez.ubicationhelper.features.map.domain.usecases.GetKeys
import com.neoris.eeomartinez.ubicationhelper.features.map.domain.usecases.GetLocation
import com.neoris.eeomartinez.ubicationhelper.features.map.domain.usecases.GetRoutes
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

class ViewModelMap @Inject constructor(private val getKeys: GetKeys,
                                       private val getLocation: GetLocation,
                                       private val getRoutes: GetRoutes): ViewModel() {

    val keys: PublishSubject<String> = PublishSubject.create()
    val locations: PublishSubject<DataSnapshot> = PublishSubject.create()
    val routes: PublishSubject<Route> = PublishSubject.create()

    fun loadDriverKeys() = getKeys(None()){ it.subscribe(keys) }
    fun observeLocations() = getLocation(None()){it.subscribe(locations)}
    fun getRoutes(driverId: String) = getRoutes(driverId){it.subscribe(routes)}
}