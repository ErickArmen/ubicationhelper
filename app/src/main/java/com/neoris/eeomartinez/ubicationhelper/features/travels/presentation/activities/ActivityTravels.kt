package com.neoris.eeomartinez.ubicationhelper.features.travels.presentation.activities

import android.arch.lifecycle.ViewModelProvider
import android.arch.paging.PagedList
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.widget.Toast
import com.neoris.eeomartinez.ubicationhelper.R
import com.neoris.eeomartinez.ubicationhelper.core.extensions.observe
import com.neoris.eeomartinez.ubicationhelper.core.extensions.toast
import com.neoris.eeomartinez.ubicationhelper.core.extensions.viewModel
import com.neoris.eeomartinez.ubicationhelper.features.travels.presentation.recyclers.RecyclerTravels
import com.neoris.eeomartinez.ubicationhelper.features.travels.presentation.viewmodels.ViewModelTravels
import dagger.android.AndroidInjection
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_travels.*
import javax.inject.Inject


const val extraEventDoc = "extraEventDoc"

class ActivityTravels: AppCompatActivity() {

    @Inject lateinit var vmFactory: ViewModelProvider.Factory
    private lateinit var viewModelTravels: ViewModelTravels
    private val disposable = CompositeDisposable()
    private lateinit var adapter: RecyclerTravels

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_travels)

        setRecycler()
        viewModelTravels = viewModel(vmFactory){
            observe(disposable, travelList2,
                    onNext = ::setAdapterList,
                    onError = ::showError)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable.clear()
        viewModelTravels.onDestroy()
    }

    private fun setRecycler() {
        rv_travels.layoutManager = LinearLayoutManager(this)
        adapter = RecyclerTravels()
        adapter.mListener = { clickOnAdapterItem(it) }
        rv_travels.adapter = adapter
    }

    private fun setAdapterList(pagedList: PagedList<String>) = adapter.submitList(pagedList)

    private fun showError(throwable: Throwable) = toast(throwable.message, Toast.LENGTH_LONG)

    fun clickOnAdapterItem(item: String) {
        val intent = Intent(this, ActivityKardex::class.java)
        intent.putExtra(extraEventDoc, item)
        startActivity(intent)
    }
}