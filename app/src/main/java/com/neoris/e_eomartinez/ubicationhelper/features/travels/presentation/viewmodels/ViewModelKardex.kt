package com.neoris.e_eomartinez.ubicationhelper.features.travels.presentation.viewmodels

import com.neoris.e_eomartinez.ubicationhelper.features.travels.domain.usecases.GetKardex
import javax.inject.Inject

class ViewModelKardex @Inject constructor(private val getKardex: GetKardex) {

    fun getKardex(documentId: String) = getKardex.getKardex(documentId)
}