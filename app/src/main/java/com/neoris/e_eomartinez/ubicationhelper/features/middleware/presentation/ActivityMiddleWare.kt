package com.neoris.e_eomartinez.ubicationhelper.features.middleware.presentation

import android.os.Bundle
import android.view.View
import android.widget.TimePicker
import android.widget.Toast
import com.neoris.e_eomartinez.ubicationhelper.R
import com.neoris.e_eomartinez.ubicationhelper.features.middleware.domain.models.Event
import dagger.android.DaggerActivity
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_middleware.*
import java.util.*
import javax.inject.Inject

class ActivityMiddleWare: DaggerActivity(), View.OnClickListener, TimePicker.OnTimeChangedListener {

    @Inject
    lateinit var        viewModel: ViewModelMiddleWare
    private val         disposable = CompositeDisposable()
    private var         hour = 0
    private var         minutes = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_middleware)

        setClickListeners()
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable.clear()
    }

    private fun sendEventToDB(name: String, time: Date) {

        disposable.add(viewModel.sendEventToDB(Event("driver_1", name, time)).subscribe(
                {
                    Toast.makeText(this@ActivityMiddleWare, "Event sent", Toast.LENGTH_LONG).show()
                },
                {
                    Toast.makeText(this@ActivityMiddleWare, it.toString(), Toast.LENGTH_LONG).show()
                }))
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
