package com.neoris.e_eomartinez.ubicationhelper.features.middleware.domain.usecases

import com.neoris.e_eomartinez.ubicationhelper.core.types.UseCase
import com.neoris.e_eomartinez.ubicationhelper.features.middleware.data.RepositoryMiddleWare
import com.neoris.e_eomartinez.ubicationhelper.features.middleware.domain.models.Event
import io.reactivex.Completable
import javax.inject.Inject

class SetEvent @Inject constructor(private val repository: RepositoryMiddleWare): UseCase {

    fun sendEventToFirestore(param: Event): Completable = repository.setData(param)

}