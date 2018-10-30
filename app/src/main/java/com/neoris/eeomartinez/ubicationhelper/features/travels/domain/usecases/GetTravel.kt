package com.neoris.eeomartinez.ubicationhelper.features.travels.domain.usecases

import com.neoris.eeomartinez.ubicationhelper.core.types.UseCase
import com.neoris.eeomartinez.ubicationhelper.core.types.UseCase.None
import com.neoris.eeomartinez.ubicationhelper.features.travels.data.RepositoryTravels
import io.reactivex.Observable
import javax.inject.Inject

class GetTravel @Inject constructor(private val repository: RepositoryTravels): UseCase<None, Observable<String>>{

    private val list = mutableListOf<String>()

    override fun run(param: None): Observable<String> = repository.getData().filter {
        var itemRepeated = true
        for (i in list.size-1 downTo 0){
            if (it.equals(list.get(i))){
                itemRepeated = false
                break
            }
        }
        list.add(it)
        itemRepeated
    }
}