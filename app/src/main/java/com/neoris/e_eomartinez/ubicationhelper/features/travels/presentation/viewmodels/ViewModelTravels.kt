package com.neoris.e_eomartinez.ubicationhelper.features.travels.presentation.viewmodels

import android.arch.lifecycle.ViewModel
import com.neoris.e_eomartinez.ubicationhelper.core.types.UseCase.None
import com.neoris.e_eomartinez.ubicationhelper.features.travels.domain.usecases.GetTravel
import io.reactivex.Observable
import javax.inject.Inject

class ViewModelTravels @Inject constructor(private val getTravel: GetTravel):ViewModel() {

    fun getTravels(): Observable<String> = getTravel.run(None())

}