package com.neoris.e_eomartinez.ubicationhelper.features.travels.domain.usecases

import com.neoris.e_eomartinez.ubicationhelper.core.types.UseCase
import com.neoris.e_eomartinez.ubicationhelper.features.travels.data.RepositoryTravels
import io.reactivex.Observable
import javax.inject.Inject

class GetTravel @Inject constructor(private val repository: RepositoryTravels): UseCase {

    fun getTravel(): Observable<String> = repository.getData()

}