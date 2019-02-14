package com.gudino.clima_ui.clima_ui.di

import com.gudino.clima_ui.clima_ui.viewmodel.WeatherViewModel
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [(NetworkModule::class)])
interface NetworkComponent {
    /**
     * Injects required dependencies into the specified WeatherViewModel.
     * @param weatherViewModel WeatherViewModel in which to inject the dependencies
     */
    fun inject(weatherViewModel: WeatherViewModel)

    @Component.Builder
    interface Builder {

        fun build(): NetworkComponent

        fun networkModule(networkModule: NetworkModule): Builder
    }
}