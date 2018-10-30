package com.neoris.eeomartinez.ubicationhelper.features.middleware.presentation

import android.arch.lifecycle.ViewModel
import com.neoris.eeomartinez.ubicationhelper.features.middleware.domain.models.Event
import com.neoris.eeomartinez.ubicationhelper.features.middleware.domain.usecases.SetEvent
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

class ViewModelMiddleWare @Inject constructor(private val setEvent: SetEvent): ViewModel() {

    var event: PublishSubject<Event> = PublishSubject.create()

    fun sendEventToDB(param: Event) = setEvent(param){
        it.toObservable<Event>().subscribe(event)
    }

}