package com.neoris.eeomartinez.ubicationhelper.features.middleware.presentation

import android.arch.lifecycle.ViewModelProvider
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.TimePicker
import android.widget.Toast
import com.neoris.eeomartinez.ubicationhelper.R
import com.neoris.eeomartinez.ubicationhelper.core.extensions.observe
import com.neoris.eeomartinez.ubicationhelper.core.extensions.toast
import com.neoris.eeomartinez.ubicationhelper.core.extensions.viewModel
import com.neoris.eeomartinez.ubicationhelper.features.middleware.domain.models.Event
import dagger.android.AndroidInjection
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_middleware.*
import java.util.*
import javax.inject.Inject

class ActivityMiddleWare: AppCompatActivity(), View.OnClickListener, TimePicker.OnTimeChangedListener {

    companion object {
        private const val driverId = "driver_1"
    }

    @Inject
    lateinit var        vmFactory: ViewModelProvider.Factory
    private lateinit var viewModelMW: ViewModelMiddleWare
    private val         disposable = CompositeDisposable()
    private var         hour = 0
    private var         minutes = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_middleware)

        setClickListeners()
        viewModelMW = viewModel(vmFactory){
            observe(disposable, event,
                    onNext = ::showSuccess,
                    onError = ::showError)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable.clear()
    }

    private fun sendEventToDB() {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, hour)
        calendar.set(Calendar.MINUTE, minutes)
        viewModelMW.sendEventToDB(Event(driverId, et_name_mw.text.toString(), calendar.time))
    }

    private fun showSuccess(event: Event) = toast(getString(R.string.event_sent, event.name)
            , Toast.LENGTH_LONG)

    private fun showError(throwable: Throwable) = toast(throwable.message, Toast.LENGTH_LONG)

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
            R.id.btn_send_mw -> sendEventToDB()
        }
    }
}
