package com.neoris.e_eomartinez.ubicationhelper

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import kotlinx.android.synthetic.main.activity_time_line.*
import java.util.ArrayList

class ActivityTimeLine: AppCompatActivity(), InterfaceListener {

    private lateinit var title: String
    private lateinit var list: MutableList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_time_line)

        getIntents()
        toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.colorWhite))
        setSupportActionBar(toolbar)
        supportActionBar?.setTitle(title)

        list = ArrayList()
        list.add("Bus Station")
        list.add("Coffee Shop")
        list.add("Car Wash")
        list.add("Church")
        list.add("Park")
        list.add("Restaurant")
        list.add("Shop")

        recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@ActivityTimeLine)
            adapter = RecyclerCustom(list, this@ActivityTimeLine)
        }
    }

    private fun getIntents(){
        title = intent.getStringExtra("title")
    }

    override fun click(position: Int) {
        goToJourneys(list[position])
    }

    private fun goToJourneys(journey: String){
        val intent = Intent(this, ActivityJourneys::class.java)
        intent.putExtra("journey", journey)
        startActivity(intent)
    }

}