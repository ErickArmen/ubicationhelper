package com.neoris.eeomartinez.ubicationhelper.features.travels.presentation.activities

import android.arch.lifecycle.ViewModelProvider
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.widget.Toast
import com.neoris.eeomartinez.ubicationhelper.R
import com.neoris.eeomartinez.ubicationhelper.core.extensions.observe
import com.neoris.eeomartinez.ubicationhelper.core.extensions.toast
import com.neoris.eeomartinez.ubicationhelper.core.extensions.viewModel
import com.neoris.eeomartinez.ubicationhelper.core.network.InterfaceListener
import com.neoris.eeomartinez.ubicationhelper.features.travels.domain.models.Kardex
import com.neoris.eeomartinez.ubicationhelper.features.travels.presentation.recyclers.RecyclerKardex
import com.neoris.eeomartinez.ubicationhelper.features.travels.presentation.viewmodels.ViewModelKardex
import dagger.android.AndroidInjection
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_kardex.*
import javax.inject.Inject

class ActivityKardex: AppCompatActivity(), InterfaceListener {

    @Inject
    lateinit var vmFactory: ViewModelProvider.Factory
    private lateinit var viewModelKardex: ViewModelKardex
    private lateinit var adapter: RecyclerKardex
    private val disposable = CompositeDisposable()
    private lateinit var documentId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kardex)

        img_header.setImageResource(R.drawable.ic_laptop_mockup)
        getIntents()
        setRecycler()
        viewModelKardex = viewModel(vmFactory){
            observe(disposable, kardex,
                    onNext = ::addItemToRecycler,
                    onError = ::showError)
        }
        viewModelKardex.getKardexItem(documentId)
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable.clear()
    }

    private fun addItemToRecycler(kardex: Kardex){
        adapter.addItem(kardex)
        adapter.notifyItemInserted(adapter.itemCount)
    }

    private fun showError(throwable: Throwable) = toast(throwable.message, Toast.LENGTH_LONG)

    private fun setRecycler(){
        rv_kardex.layoutManager = LinearLayoutManager(this)
        rv_kardex.isNestedScrollingEnabled = false
        adapter = RecyclerKardex(this)
        rv_kardex.adapter = adapter
    }

    private fun getIntents() {
        documentId = intent.extras.getString(extraEventDoc)
    }

    override fun click(position: Int) {

    }
}