package com.neoris.e_eomartinez.ubicationhelper.features.travels.presentation.viewmodels

import com.neoris.e_eomartinez.ubicationhelper.features.travels.domain.usecases.GetTravel
import io.reactivex.Observable
import javax.inject.Inject

class ViewModelTravels @Inject constructor(private val getTravel: GetTravel) {

    fun getTravels(): Observable<String> = getTravel.getTravel()

}