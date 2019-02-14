package com.gudino.clima_ui.clima_ui.fragment

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.SearchView
import android.view.*
import android.view.inputmethod.InputMethodManager
import com.gudino.clima_ui.clima_ui.R
import com.gudino.clima_ui.clima_ui.adapter.CitiesAdapter
import com.gudino.clima_ui.clima_ui.model.UIItem
import com.gudino.clima_ui.clima_ui.viewmodel.WeatherViewModel
import kotlinx.android.synthetic.main.weather_detail_fragment.*

/**
 * Displays screen with search after user clicks on add city in the right panel
 */
class AddCityFragment : Fragment(), CitiesAdapter.Action {

    private lateinit var viewModel: WeatherViewModel
    private lateinit var adapter: CitiesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        viewModel = activity?.run {
            ViewModelProviders.of(this).get(WeatherViewModel::class.java)
        } ?: throw Exception("Invalid Activity")

        viewModel.weatherResponseByCityName.observe(this, Observer {
            adapter.addCityFromSearch(it)
        })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.weather_detail_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        adapter = CitiesAdapter(this)
        list.layoutManager = LinearLayoutManager(activity)
        list.addItemDecoration(DividerItemDecoration(activity, DividerItemDecoration.VERTICAL))
        list.adapter = adapter
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu, menu)
        val searchMenu = menu.findItem(R.id.action_search)
        val searchView = searchMenu.actionView as SearchView
        searchView.queryHint = resources.getString(R.string.search)

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    closeKeyboard()
                    viewModel.getCityByName(it)
                }
                return true
            }

            override fun onQueryTextChange(query: String?) = false
        })

        super.onCreateOptionsMenu(menu, inflater)
    }

    private fun closeKeyboard() {
        val imm = activity!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view!!.windowToken, 0)
    }

    override fun tap() = Unit

    override fun tapAndAdd(uiItem: UIItem) {
        viewModel.addCityToList(uiItem)
    }
}