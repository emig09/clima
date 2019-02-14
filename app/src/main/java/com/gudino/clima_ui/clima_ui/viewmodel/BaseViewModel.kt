package com.gudino.clima_ui.clima_ui.viewmodel

import android.arch.lifecycle.ViewModel
import com.gudino.clima_ui.clima_ui.di.DaggerNetworkComponent
import com.gudino.clima_ui.clima_ui.di.NetworkComponent
import com.gudino.clima_ui.clima_ui.di.NetworkModule

open class BaseViewModel : ViewModel() {

    private val injector: NetworkComponent = DaggerNetworkComponent
            .builder()
            .networkModule(NetworkModule)
            .build()

    init {
        inject()
    }

    /**
     * Injects the required dependencies
     */
    private fun inject() {
        when (this) {
            is WeatherViewModel -> injector.inject(this)
        }
    }
}