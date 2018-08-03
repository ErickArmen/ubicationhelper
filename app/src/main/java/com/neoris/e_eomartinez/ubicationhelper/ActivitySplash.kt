package com.neoris.e_eomartinez.ubicationhelper

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.animation.AnimationUtils
import kotlinx.android.synthetic.main.activity_splash.*

class ActivitySplash: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val thread = Thread({
            val animation = AnimationUtils.loadAnimation(this, R.anim.fade_anim)
            text_splash.startAnimation(animation)
            Thread.sleep(2200)
            startActivity(Intent(this, ActivityMain::class.java))
            finish()
        })
        thread.start()
    }
}