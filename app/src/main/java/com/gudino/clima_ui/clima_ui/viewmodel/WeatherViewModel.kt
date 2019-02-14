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

    /**
     * Returns weather given the lat-long provided (the one got at the time user opens the app)
     */
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

    /**
     * Return cities given a query when user introduce it in the search view
     */
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

    /**
     * Returns 5 day weather for a specific city, given an id
     */
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