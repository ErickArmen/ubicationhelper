package com.neoris.eeomartinez.ubicationhelper.features.travels.presentation.activities

import android.arch.lifecycle.ViewModelProvider
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.widget.Toast
import com.neoris.eeomartinez.ubicationhelper.R
import com.neoris.eeomartinez.ubicationhelper.core.extensions.observe
import com.neoris.eeomartinez.ubicationhelper.core.extensions.toast
import com.neoris.eeomartinez.ubicationhelper.core.extensions.viewModel
import com.neoris.eeomartinez.ubicationhelper.core.network.InterfaceListener
import com.neoris.eeomartinez.ubicationhelper.features.travels.presentation.recyclers.RecyclerTravels
import com.neoris.eeomartinez.ubicationhelper.features.travels.presentation.viewmodels.ViewModelTravels
import dagger.android.AndroidInjection
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_travels.*
import javax.inject.Inject


const val extraEventDoc = "extraEventDoc"

class ActivityTravels: AppCompatActivity(), InterfaceListener {

    @Inject lateinit var vmFactory: ViewModelProvider.Factory
    private lateinit var viewModelTravels: ViewModelTravels
    private val disposable = CompositeDisposable()
    private lateinit var adapter: RecyclerTravels

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_travels)

        viewModelTravels = viewModel(vmFactory){
            observe(disposable, travels,
                    onNext = ::addItemToRecycler,
                    onError = ::showError)
        }
        setRecycler()
        viewModelTravels.getTravels()
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable.clear()
    }

    private fun setRecycler() {
        rv_travels.layoutManager = LinearLayoutManager(this)
        adapter = RecyclerTravels(this)
        rv_travels.adapter = adapter
    }

    private fun addItemToRecycler(travelId: String){
        adapter.addItem(travelId)
        adapter.notifyItemInserted(adapter.itemCount)
    }

    private fun showError(throwable: Throwable) = toast(throwable.message, Toast.LENGTH_LONG)

    override fun click(position: Int) {
        val item = adapter.getItem(position)
        val intent = Intent(this, ActivityKardex::class.java)
        intent.putExtra(extraEventDoc, item)
        startActivity(intent)
    }
}