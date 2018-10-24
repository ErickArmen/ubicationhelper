package com.neoris.e_eomartinez.ubicationhelper.features.middleware.presentation

import android.arch.lifecycle.ViewModel
import com.neoris.e_eomartinez.ubicationhelper.features.middleware.domain.models.Event
import com.neoris.e_eomartinez.ubicationhelper.features.middleware.domain.usecases.SetEvent
import io.reactivex.Completable
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

class ViewModelMiddleWare @Inject constructor(private val setEvent: SetEvent): ViewModel() {

    fun sendEventToDB(param: Event): Completable = setEvent.run(param)

}