package com.neoris.eeomartinez.ubicationhelper.features.travels.presentation.viewmodels

import android.arch.lifecycle.ViewModel
import com.neoris.eeomartinez.ubicationhelper.core.types.UseCase.None
import com.neoris.eeomartinez.ubicationhelper.features.travels.domain.usecases.GetTravel
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

class ViewModelTravels @Inject constructor(private val getTravel: GetTravel):ViewModel() {

    val travels: PublishSubject<String> = PublishSubject.create()

    fun getTravels() = getTravel(None()){ it.subscribe(travels) }

}