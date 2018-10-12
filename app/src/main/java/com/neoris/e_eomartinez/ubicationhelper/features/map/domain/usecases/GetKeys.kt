package com.neoris.e_eomartinez.ubicationhelper.features.map.domain.usecases

import com.neoris.e_eomartinez.ubicationhelper.core.types.UseCase
import com.neoris.e_eomartinez.ubicationhelper.features.map.data.RepositoryMap
import io.reactivex.Observable
import javax.inject.Inject

class GetKeys @Inject constructor(private val repository: RepositoryMap): UseCase {

    fun getKeys(): Observable<String> = repository.getData()

}