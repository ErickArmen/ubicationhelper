package com.neoris.e_eomartinez.ubicationhelper

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import kotlinx.android.synthetic.main.activity_prepare.*

const val extraDescription = "extraDescription"
const val extraQuantity = "extraQuantity"

class ActivityPrepare: AppCompatActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_prepare)

        btn_ready.setOnClickListener(this)
    }

    private fun deliveryIsReady(){
        if (et_description.text.toString().isNotBlank() && et_quantity.text.toString().isNotBlank()){
            val intent = Intent()
            intent.putExtra(extraDescription, et_description.text.toString())
            intent.putExtra(extraQuantity, et_quantity.text.toString())
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
    }

    override fun onClick(p0: View?) {
        when(p0?.id){
            R.id.btn_ready -> deliveryIsReady()
        }
    }
}