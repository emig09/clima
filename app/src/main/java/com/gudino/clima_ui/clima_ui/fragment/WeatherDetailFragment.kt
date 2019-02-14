package com.gudino.clima_ui.clima_ui.fragment

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import com.gudino.clima_ui.clima_ui.R
import com.gudino.clima_ui.clima_ui.adapter.WeatherDetailAdapter
import com.gudino.clima_ui.clima_ui.model.WeatherResponse
import com.gudino.clima_ui.clima_ui.viewmodel.WeatherViewModel
import kotlinx.android.synthetic.main.weather_detail_fragment.*

/**
 * Displays city name, datetime and temperature for an specific city
 */
class WeatherDetailFragment : Fragment() {

    private lateinit var viewModel: WeatherViewModel
    private lateinit var adapter: WeatherDetailAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = activity?.run {
            ViewModelProviders.of(this).get(WeatherViewModel::class.java)
        } ?: throw Exception("Invalid Activity")

        viewModel.weatherResponseByCityId.observe(this, Observer {
            it?.let { adapter.loadDetail(it) }
        })

        viewModel.weatherResponseByCity.observe(this, Observer {
            it?.let { adapter.loadDetail(it) }
        })

        viewModel.errors.observe(this, Observer {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
        })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
            inflater.inflate(R.layout.weather_detail_fragment, container, false)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        adapter = WeatherDetailAdapter()
        list.layoutManager = LinearLayoutManager(activity)
        list.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        list.adapter = adapter
    }
}