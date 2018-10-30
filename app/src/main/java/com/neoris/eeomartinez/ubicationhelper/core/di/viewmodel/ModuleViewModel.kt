package com.neoris.eeomartinez.ubicationhelper.core.di.viewmodel

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.neoris.eeomartinez.ubicationhelper.features.map.presentation.viewmodels.ViewModelMap
import com.neoris.eeomartinez.ubicationhelper.features.middleware.presentation.ViewModelMiddleWare
import com.neoris.eeomartinez.ubicationhelper.features.travels.presentation.viewmodels.ViewModelKardex
import com.neoris.eeomartinez.ubicationhelper.features.travels.presentation.viewmodels.ViewModelTravels
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface ModuleViewModel {

    @Binds
    fun provideFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(ViewModelMap::class)
    fun bindMap(viewModel: ViewModelMap): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ViewModelMiddleWare::class)
    fun bindMW(viewModel: ViewModelMiddleWare): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ViewModelKardex::class)
    fun bindKardex(viewModel: ViewModelKardex): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ViewModelTravels::class)
    fun bindTravels(viewModel: ViewModelTravels): ViewModel


}