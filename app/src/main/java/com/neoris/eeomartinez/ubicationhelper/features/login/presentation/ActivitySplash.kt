package com.neoris.eeomartinez.ubicationhelper.features.login.presentation


import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.neoris.eeomartinez.ubicationhelper.features.map.presentation.activities.ActivityMain
import com.neoris.eeomartinez.ubicationhelper.R


class ActivitySplash: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val thread = Thread {
            Thread.sleep(8000)
            goToMain()
        }
        thread.start()
    }

    private fun goToMain(){
        val intent = Intent(this, ActivityMain::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }
}