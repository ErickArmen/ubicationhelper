package com.neoris.e_eomartinez.ubicationhelper.features.travels.domain.usecases

import com.neoris.e_eomartinez.ubicationhelper.core.types.UseCase
import com.neoris.e_eomartinez.ubicationhelper.features.travels.data.RepositoryKardex
import com.neoris.e_eomartinez.ubicationhelper.features.travels.domain.models.Kardex
import io.reactivex.Observable
import javax.inject.Inject

class GetKardex @Inject constructor(private val repository: RepositoryKardex): UseCase<String, Observable<Kardex>> {

    override fun run(param: String): Observable<Kardex> = repository.getData(param)

}