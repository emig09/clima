package com.gudino.clima_ui.clima_ui.viewmodel

import android.arch.lifecycle.MutableLiveData
import com.gudino.clima_ui.clima_ui.api.WeatherApi
import com.gudino.clima_ui.clima_ui.model.CityResponse
import com.gudino.clima_ui.clima_ui.model.UIItem
import com.gudino.clima_ui.clima_ui.model.WeatherResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.UnknownHostException
import javax.inject.Inject

class WeatherViewModel : BaseViewModel() {

    @Inject
    lateinit var weatherApi: WeatherApi

    val weatherResponseByCity = MutableLiveData<WeatherResponse>()
    val weatherResponseByCityId = MutableLiveData<WeatherResponse>()
    val weatherResponseByCityName = MutableLiveData<CityResponse>()
    val cityAddedToList = MutableLiveData<UIItem>()

    val errors = MutableLiveData<String>()

    fun getCityByLocation(latitude: Double, longitude: Double) {
        weatherApi.getWeatherDataByLocation(latitude, longitude).enqueue(object : Callback<WeatherResponse> {

            override fun onResponse(call: Call<WeatherResponse>?, response: Response<WeatherResponse>?) {
                weatherResponseByCity.value = response?.body()
            }

            override fun onFailure(call: Call<WeatherResponse>?, t: Throwable?) {
                when (t) {
                    is UnknownHostException -> errors.value = "no connectivity"
                    else -> errors.value = "unknown error"
                }
            }
        })
    }

    fun getCityByName(cityName: String) {
        weatherApi.getCitiesByName(cityName).enqueue(object : Callback<CityResponse> {

            override fun onResponse(call: Call<CityResponse>?, response: Response<CityResponse>?) {
                weatherResponseByCityName.value = response?.body()
            }

            override fun onFailure(call: Call<CityResponse>?, t: Throwable?) {
                when (t) {
                    is UnknownHostException -> errors.value = "no connectivity"
                    else -> errors.value = "unknown error"
                }
            }
        })
    }

    fun getCityById(id: Int) {
        weatherApi.getWeatherDataById(id).enqueue(object : Callback<WeatherResponse> {

            override fun onResponse(call: Call<WeatherResponse>?, response: Response<WeatherResponse>?) {
                weatherResponseByCityId.value = response?.body()
            }

            override fun onFailure(call: Call<WeatherResponse>?, t: Throwable?) {
                when (t) {
                    is UnknownHostException -> errors.value = "no connectivity"
                    else -> errors.value = "unknown error"
                }
            }
        })
    }

    fun addCityToList(uiItem: UIItem?) {
        cityAddedToList.value = uiItem
    }
}