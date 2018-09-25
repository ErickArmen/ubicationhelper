package com.neoris.e_eomartinez.ubicationhelper

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_travels.*


const val extraEventDoc = "extraEventDoc"

class ActivityTravels: AppCompatActivity(), InterfaceListener {

    private val firestore = FirebaseFirestore.getInstance()
    private val workDoneList: MutableList<String> =  mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_travels)

        getFirestoreList()
    }

    private fun getFirestoreList() {
        firestore.collection("driver_1").get().addOnCompleteListener {
            if (it.isSuccessful){
                it.result.documents.forEach {doc ->
                    workDoneList.add(doc.id)
                }
                setRecycler(workDoneList)
            }
        }
    }

    private fun setRecycler(list: List<String>) {
        rv_travels.layoutManager = LinearLayoutManager(this)
        rv_travels.addItemDecoration(DividerItemDecoration(this, LinearLayoutManager.VERTICAL))
        rv_travels.adapter = RecyclerWorkDone(list, this)
    }

    override fun click(position: Int) {
        val item = (rv_travels.adapter as RecyclerWorkDone).getItem(position)
        if(item.isNotBlank()) {
            val intent = Intent(this, ActivityKardex::class.java)
            intent.putExtra(extraEventDoc, item)
            startActivity(intent)
        }
    }
}