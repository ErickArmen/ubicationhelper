package com.neoris.eeomartinez.ubicationhelper.features.map.domain.usecases

import com.neoris.eeomartinez.ubicationhelper.core.types.UseCase
import com.neoris.eeomartinez.ubicationhelper.features.map.data.RepositoryCloudRoutes
import com.neoris.eeomartinez.ubicationhelper.features.map.domain.models.RawRoute.Route
import io.reactivex.Observable
import javax.inject.Inject

class GetRoutes @Inject constructor(val repository: RepositoryCloudRoutes):
        UseCase<String, Observable<Route>> {

    override fun run(param: String): Observable<Route> =
            repository.getData(param).map{ it.transformtoRoute() }
}