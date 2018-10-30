package com.neoris.eeomartinez.ubicationhelper.features.map.domain.usecases

import com.neoris.eeomartinez.ubicationhelper.core.types.UseCase
import com.neoris.eeomartinez.ubicationhelper.core.types.UseCase.None
import com.neoris.eeomartinez.ubicationhelper.features.map.data.RepositoryCloudKeys
import io.reactivex.Observable
import javax.inject.Inject

class GetKeys @Inject constructor(private val repository: RepositoryCloudKeys): UseCase<None, Observable<String>> {

    override fun run(param: None): Observable<String> = repository.getData()
}