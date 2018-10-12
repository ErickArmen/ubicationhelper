package com.neoris.e_eomartinez.ubicationhelper.features.journeys.presentation.activities

import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import com.neoris.e_eomartinez.ubicationhelper.core.network.InterfaceListener
import com.neoris.e_eomartinez.ubicationhelper.R
import com.neoris.e_eomartinez.ubicationhelper.features.journeys.presentation.recyclers.RecyclerCustom
import kotlinx.android.synthetic.main.activity_time_line.*
import java.util.ArrayList

const val journey_extra = "journey"

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

        list = arrayListOf("Bus Station", "Coffee Shop", "Car Wash", "Church", "Park", "Restaurant", "Shop")

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
        intent.putExtra(journey_extra, journey)
        startActivity(intent)
    }

}