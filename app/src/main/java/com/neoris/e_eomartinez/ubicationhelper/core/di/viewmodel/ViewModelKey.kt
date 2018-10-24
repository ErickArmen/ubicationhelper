package com.neoris.e_eomartinez.ubicationhelper.core.di.viewmodel

import android.arch.lifecycle.ViewModel
import dagger.MapKey
import kotlin.reflect.KClass

@MapKey
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_GETTER)
@Retention(AnnotationRetention.RUNTIME)
internal annotation class ViewModelKey(val value: KClass<out ViewModel>)
