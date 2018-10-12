package com.neoris.e_eomartinez.ubicationhelper.features.travels.presentation.activities

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.widget.Toast
import com.neoris.e_eomartinez.ubicationhelper.R
import com.neoris.e_eomartinez.ubicationhelper.core.network.InterfaceListener
import com.neoris.e_eomartinez.ubicationhelper.features.travels.presentation.recyclers.RecyclerKardex
import com.neoris.e_eomartinez.ubicationhelper.features.travels.presentation.viewmodels.ViewModelKardex
import dagger.android.DaggerActivity
import kotlinx.android.synthetic.main.activity_kardex.*
import javax.inject.Inject

class ActivityKardex: DaggerActivity(), InterfaceListener {

    @Inject
    lateinit var viewModel: ViewModelKardex
    private lateinit var adapter: RecyclerKardex
    private lateinit var documentId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kardex)

        img_header.setImageResource(R.drawable.ic_laptop_mockup)
        getIntents()
        setRecycler()
        fillListForRecycler()
    }

    private fun fillListForRecycler(){

        viewModel.getKardex(documentId).subscribe(
                {
                    adapter.addItem(it)
                    adapter.notifyItemInserted(adapter.itemCount)
                },
                {
                    Toast.makeText(this@ActivityKardex, it.message, Toast.LENGTH_LONG).show()
                }
        )
    }

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