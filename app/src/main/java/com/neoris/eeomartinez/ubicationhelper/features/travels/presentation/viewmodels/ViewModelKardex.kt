package com.neoris.eeomartinez.ubicationhelper.features.travels.presentation.viewmodels

import android.arch.lifecycle.ViewModel
import com.neoris.eeomartinez.ubicationhelper.features.travels.domain.models.Kardex
import com.neoris.eeomartinez.ubicationhelper.features.travels.domain.usecases.GetKardex
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

class ViewModelKardex @Inject constructor(private val getKardex: GetKardex): ViewModel() {

    val kardex: PublishSubject<Kardex> = PublishSubject.create()

    fun getKardexItem(documentId: String) = getKardex(documentId){ it.subscribe(kardex) }
}