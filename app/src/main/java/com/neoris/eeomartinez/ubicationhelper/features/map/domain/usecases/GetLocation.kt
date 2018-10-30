package com.neoris.eeomartinez.ubicationhelper.features.map.domain.usecases

import com.google.firebase.database.DataSnapshot
import com.neoris.eeomartinez.ubicationhelper.core.types.UseCase
import com.neoris.eeomartinez.ubicationhelper.core.types.UseCase.None
import com.neoris.eeomartinez.ubicationhelper.features.map.data.RepositoryCloudLocations
import io.reactivex.Observable
import javax.inject.Inject

class GetLocation @Inject constructor(val repository: RepositoryCloudLocations):
        UseCase<None, Observable<DataSnapshot>> {

    override fun run(param: None): Observable<DataSnapshot> = repository.getData()
}