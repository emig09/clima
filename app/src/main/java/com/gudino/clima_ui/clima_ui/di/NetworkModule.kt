package com.gudino.clima_ui.clima_ui.di

import com.gudino.clima_ui.clima_ui.api.WeatherApi
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
object NetworkModule {

    private const val OPEN_WEATHER_BASE_URL = "https://api.openweathermap.org/"

    /**
     * Provides the WeatherApi service implementation.
     * @param retrofit the Retrofit object used to instantiate the service
     * @return the WeatherApi service implementation.
     */
    @Provides
    internal fun provideWeatherApi(retrofit: Retrofit) = retrofit.create(WeatherApi::class.java)

    /**
     * Provides the Retrofit object.
     * @return the Retrofit object
     */
    @Provides
    internal fun provideRetrofitInterface() =
            Retrofit.Builder()
                    .baseUrl(OPEN_WEATHER_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
}