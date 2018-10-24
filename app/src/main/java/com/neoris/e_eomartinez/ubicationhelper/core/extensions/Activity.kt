package com.neoris.e_eomartinez.ubicationhelper.core.extensions

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider.Factory
import android.arch.lifecycle.ViewModelProviders
import android.support.v7.app.AppCompatActivity
import android.widget.Toast



inline fun <reified T : ViewModel> AppCompatActivity.viewModel(factory: Factory ,body: T.() -> Unit): T {
    val vm = ViewModelProviders.of(this, factory)[T::class.java]
    vm.body()
    return vm
}

fun AppCompatActivity.toast(message: CharSequence?, duration: Int) = Toast.makeText(this, message, duration).show()
