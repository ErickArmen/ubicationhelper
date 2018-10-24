package com.neoris.e_eomartinez.ubicationhelper.features.middleware.domain.usecases

import com.neoris.e_eomartinez.ubicationhelper.core.types.UseCase
import com.neoris.e_eomartinez.ubicationhelper.features.middleware.data.RepositoryCloudMW
import com.neoris.e_eomartinez.ubicationhelper.features.middleware.domain.models.Event
import io.reactivex.Completable
import javax.inject.Inject

class SetEvent @Inject constructor(private val repository: RepositoryCloudMW): UseCase<Event, Completable>{


    override fun run(param: Event): Completable = repository.setData(param)
}