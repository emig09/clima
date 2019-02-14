package com.gudino.clima_ui.clima_ui

import android.Manifest
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import com.gudino.clima_ui.clima_ui.adapter.CitiesAdapter
import com.gudino.clima_ui.clima_ui.fragment.AddCityFragment
import com.gudino.clima_ui.clima_ui.fragment.WeatherDetailFragment
import com.gudino.clima_ui.clima_ui.model.UIItem
import com.gudino.clima_ui.clima_ui.model.UIItem.Companion.TEXT_TYPE
import com.gudino.clima_ui.clima_ui.model.UIItem.Companion.TITLE_TYPE
import com.gudino.clima_ui.clima_ui.viewmodel.WeatherViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), CitiesAdapter.Action {

    companion object {
        private const val MY_PERMISSIONS_REQUEST_COARSE_LOCATION = 1
    }

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var viewModel: WeatherViewModel
    private lateinit var adapter: CitiesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProviders.of(this).get(WeatherViewModel::class.java)

        viewModel.weatherResponseByCity.observe(this, Observer {
            it?.run { adapter.addCity(UIItem(TITLE_TYPE, it.city.name.plus(", ${it.city.country}"))) }
        })

        viewModel.cityAddedToList.observe(this, Observer {
            supportFragmentManager.popBackStack()
            it?.run {
                adapter.addCity(it)
                viewModel.getCityById(it.id!!)
            }
        })

        checkLocationPermission()

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        val actionbar: ActionBar? = supportActionBar
        actionbar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_menu)
        }

        drawerLayout = findViewById(R.id.drawer_layout)

        adapter = CitiesAdapter(this)
        recycler.layoutManager = LinearLayoutManager(this)
        recycler.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        recycler.adapter = adapter

        val navigationView: NavigationView = findViewById(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener { menuItem ->
            menuItem.isChecked = true
            drawerLayout.closeDrawers()
            true
        }

        if (savedInstanceState == null) {
            showWeatherDetailFragment()
        }
    }

    private fun showWeatherDetailFragment() =
            supportFragmentManager
                    .beginTransaction()
                    .add(R.id.content_frame, WeatherDetailFragment())
                    .commit()

    override fun tap() {
        drawerLayout.closeDrawers()
        supportFragmentManager
                .beginTransaction()
                .replace(R.id.content_frame, AddCityFragment())
                .addToBackStack(null)
                .commit()
    }

    override fun tapAndAdd(uiItem: UIItem) {
        adapter.addCity(uiItem)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                drawerLayout.openDrawer(GravityCompat.START)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                            Manifest.permission.ACCESS_COARSE_LOCATION)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
                        MY_PERMISSIONS_REQUEST_COARSE_LOCATION)

                // MY_PERMISSIONS_REQUEST_COARSE_LOCATION is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            getLastKnownLocation()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            MY_PERMISSIONS_REQUEST_COARSE_LOCATION -> {
                // If request is cancelled, the result arrays are empty.
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    getLastKnownLocation()
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return
            }

        // Add other 'when' lines to check for other
        // permissions this app might request.
            else -> {
                // Ignore all other requests.
            }
        }
    }

    private fun getLastKnownLocation() {
        // Acquire a reference to the system Location Manager
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val locationProvider: String = LocationManager.NETWORK_PROVIDER
        // Or use LocationManager.GPS_PROVIDER

        val lastKnownLocation: Location = locationManager.getLastKnownLocation(locationProvider)
        viewModel.getCityByLocation(lastKnownLocation.latitude, lastKnownLocation.longitude)
    }
}
