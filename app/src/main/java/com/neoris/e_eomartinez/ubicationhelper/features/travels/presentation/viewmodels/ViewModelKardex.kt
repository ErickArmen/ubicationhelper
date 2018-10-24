package com.neoris.e_eomartinez.ubicationhelper.features.travels.presentation.viewmodels

import android.arch.lifecycle.ViewModel
import com.neoris.e_eomartinez.ubicationhelper.features.travels.domain.usecases.GetKardex
import javax.inject.Inject

class ViewModelKardex @Inject constructor(private val getKardex: GetKardex): ViewModel() {

    fun getKardex(documentId: String) = getKardex.run(documentId)
}