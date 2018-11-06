package com.neoris.eeomartinez.ubicationhelper.features.travels.presentation.recyclers

import android.arch.paging.ItemKeyedDataSource
import com.neoris.eeomartinez.ubicationhelper.features.travels.data.RepositoryTravelsList
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class DataSourceTravels @Inject constructor(private val repository: RepositoryTravelsList):
        ItemKeyedDataSource<String, String>() {

    private val composite = CompositeDisposable()

    override fun loadInitial(params: LoadInitialParams<String>, callback: LoadInitialCallback<String>) {
        composite.add(repository.getData(params.requestedInitialKey!!).subscribe {callback.onResult(it)})
    }

    override fun loadAfter(params: LoadParams<String>, callback: LoadCallback<String>) {
        composite.add(repository.getData(params.key).subscribe {callback.onResult(it)})
    }

    override fun loadBefore(params: LoadParams<String>, callback: LoadCallback<String>) { }

    override fun getKey(item: String): String = item

    fun onDestroy() = composite.clear()
}