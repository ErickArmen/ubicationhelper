package com.neoris.e_eomartinez.ubicationhelper.features.travels.presentation.activities

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.widget.Toast
import com.neoris.e_eomartinez.ubicationhelper.R
import com.neoris.e_eomartinez.ubicationhelper.core.network.InterfaceListener
import com.neoris.e_eomartinez.ubicationhelper.features.travels.presentation.recyclers.RecyclerTravels
import com.neoris.e_eomartinez.ubicationhelper.features.travels.presentation.viewmodels.ViewModelTravels
import dagger.android.AndroidInjection
import dagger.android.DaggerActivity
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_travels.*
import javax.inject.Inject


const val extraEventDoc = "extraEventDoc"

class ActivityTravels: DaggerActivity(), InterfaceListener {

    @Inject lateinit var viewModel: ViewModelTravels
    private val disposable = CompositeDisposable()
    private lateinit var adapter: RecyclerTravels

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_travels)

        setRecycler()
        getFirestoreList()
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable.clear()
    }

    private fun getFirestoreList() {

        disposable.add(viewModel.getTravels().filter {s ->
            var itemRepeated = true
            for (i in adapter.itemCount-1 downTo 0){
                if (s.equals(adapter.getItem(i))){
                    itemRepeated = false
                    break
                }
            }
            itemRepeated
        }.subscribe(
                {
                    adapter.addItem(it)
                    adapter.notifyItemInserted(adapter.itemCount)
                },
                {
                    Toast.makeText(this@ActivityTravels, it.message, Toast.LENGTH_LONG).show()
                }
        ))
    }

    private fun setRecycler() {
        rv_travels.layoutManager = LinearLayoutManager(this)
        //rv_travels.addItemDecoration(DividerItemDecoration(this, LinearLayoutManager.VERTICAL))
        adapter = RecyclerTravels(this)
        rv_travels.adapter = adapter
    }

    override fun click(position: Int) {
        val item = adapter.getItem(position)
        val intent = Intent(this, ActivityKardex::class.java)
        intent.putExtra(extraEventDoc, item)
        startActivity(intent)
    }
}