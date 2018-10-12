package com.neoris.e_eomartinez.ubicationhelper.features.middleware.presentation

import com.neoris.e_eomartinez.ubicationhelper.features.middleware.domain.models.Event
import com.neoris.e_eomartinez.ubicationhelper.features.middleware.domain.usecases.SetEvent
import io.reactivex.Completable
import javax.inject.Inject

class ViewModelMiddleWare @Inject constructor(private val setEvent: SetEvent) {

    fun sendEventToDB(param: Event): Completable = setEvent.sendEventToFirestore(param)

}