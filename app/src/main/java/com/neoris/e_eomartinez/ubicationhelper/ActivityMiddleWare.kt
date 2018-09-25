package com.neoris.e_eomartinez.ubicationhelper

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.TimePicker
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.firestore.SetOptions
import kotlinx.android.synthetic.main.activity_middleware.*
import java.util.*

class ActivityMiddleWare: AppCompatActivity(), View.OnClickListener, TimePicker.OnTimeChangedListener {

    private val firestore = FirebaseFirestore.getInstance()
    private var hour = 0
    private var minutes = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_middleware)

        setClickListeners()
    }

    private fun sendEventToDB(name: String, time: Date) {
        firestore.collection("driver_1").get().addOnCompleteListener {
            if (it.isSuccessful) {
                val size = it.result.size()
                val document = it.result.documents[size-1] as QueryDocumentSnapshot
                val eventMap = HashMap<String, Any>()
                eventMap["createdDate"] = time
                eventMap["name"] = name
                firestore.collection("driver_1").document(document.id).collection("events").document()
                        .set(eventMap, SetOptions.merge())
                Toast.makeText(this@ActivityMiddleWare, "Event sent", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun setClickListeners(){
        btn_send_mw.setOnClickListener(this)
        tp_mw.setOnTimeChangedListener(this)
    }

    override fun onTimeChanged(p0: TimePicker?, hourOfDay: Int, minute: Int) {
        hour = hourOfDay
        minutes = minute
    }

    override fun onClick(p0: View?) {
        when(p0?.id){
            R.id.btn_send_mw -> {
                val calendar = Calendar.getInstance()
                calendar.set(Calendar.HOUR_OF_DAY, hour)
                calendar.set(Calendar.MINUTE, minutes)
                sendEventToDB(et_name_mw.text.toString(), calendar.time)
            }
        }
    }
}
