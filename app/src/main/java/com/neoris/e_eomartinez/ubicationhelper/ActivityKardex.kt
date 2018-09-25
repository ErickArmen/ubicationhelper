package com.neoris.e_eomartinez.ubicationhelper

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_kardex.*
import java.text.SimpleDateFormat
import java.util.*

class ActivityKardex: AppCompatActivity(), InterfaceListener {

    private val firestore = FirebaseFirestore.getInstance()
    private val eventsList: MutableList<Kardex> = mutableListOf()
    private lateinit var documentId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kardex)

        getIntents()
        fillListForRecycler()
    }

    private fun fillListForRecycler(){
        firestore.collection("driver_1").document(documentId).collection("events")
                .orderBy("createdDate").get().addOnCompleteListener {
                    if (it.isSuccessful){
                        it.result.documents.forEach { doc ->
                            val kardexDate = doc["createdDate"] as Date
                            val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
                            eventsList.add(Kardex(doc["name"] as String, sdf.format(kardexDate)))
                        }
                        setRecycler(eventsList)
                    }
                }
    }

    private fun setRecycler(list: List<Kardex>){
        rv_kardex.layoutManager = LinearLayoutManager(this)
        rv_kardex.isNestedScrollingEnabled = false
        rv_kardex.adapter = RecyclerKardex(list, this)
    }

    private fun getIntents() {
        documentId = intent.extras.getString(extraEventDoc)
    }

    override fun click(position: Int) {
    }
}