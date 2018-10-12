package com.neoris.e_eomartinez.ubicationhelper.features.login.presentation


import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.animation.AnimationUtils
import com.neoris.e_eomartinez.ubicationhelper.features.map.presentation.activities.ActivityMain
import com.neoris.e_eomartinez.ubicationhelper.R
import kotlinx.android.synthetic.main.activity_splash.*


class ActivitySplash: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val thread = Thread({
            val animation = AnimationUtils.loadAnimation(this, R.anim.fade_anim)
            text_splash.startAnimation(animation)
            Thread.sleep(2200)
            goToMain()
        })
        thread.start()
    }

    private fun goToMain(){
        val intent = Intent(this, ActivityMain::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }
}