package com.example.weatherapp

import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.weatherapp.api.WeatherApiService
import com.example.weatherapp.api.WeatherData
import com.example.weatherapp.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class MainActivity : AppCompatActivity() {
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private lateinit var gpsLocationListener: LocationListener
    private var locationisSet: Boolean = false
    private lateinit var navController: NavController
    var lastWeatherResponse: List<WeatherData> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_content_main) as NavHostFragment
        navController = navHostFragment.navController
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)

        binding.toolbar.setTitle("Place name")

         gpsLocationListener = object : LocationListener {
            override fun onLocationChanged(location: Location) {
                if (locationisSet == false) {
                    locationisSet = true
                    lifecycleScope.launch {
                        getInfoOfLocation(location.latitude, location.longitude)
                    }
                }
            }

            override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}
            override fun onProviderEnabled(provider: String) {}
            override fun onProviderDisabled(provider: String) {}
        }
        getLocation()

        binding.fab.setOnClickListener { view ->
            gotoDashboardToSearch()
        }
    }

    fun gotoSearchToDashboard(lat: Double, long: Double) {
        navController.navigate(R.id.action_Search_to_Dashboard)
        binding.fab.show()
        lifecycleScope.launch {
            getInfoOfLocation(lat, long)
        }
    }

    fun gotoDashboardToSearch() {
        navController.navigate(R.id.action_Dashboard_to_Search)
        binding.fab.hide()
        binding.toolbar.setTitle("Search")
    }

    suspend fun getInfoOfLocation(lat: Double, long: Double) {
        val apiKey = getResources().getString(R.string.weather_api_key)

        // Retrofit setup
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.openweathermap.org/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(WeatherApiService::class.java)

        try {
            val response = withContext(Dispatchers.IO) {
                service.getWeatherForecast(lat, long, apiKey)
            }

            binding.toolbar.setTitle(response.city.name)
            lastWeatherResponse = response.list

            val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_content_main) as? NavHostFragment
            val dashboardFragment = navHostFragment?.childFragmentManager?.fragments?.firstOrNull() as? DashboardFragment
            dashboardFragment?.setResponse(response.list)
        } catch (e: Exception) {
            e.printStackTrace()
            println("Failed to fetch weather data: ${e.message}")
        }
    }

    fun getLocation() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                PERMISSION_REQUEST_ACCESS_FINE_LOCATION)
            return
        }
        val locationManager: LocationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0L, 0f, gpsLocationListener)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_ACCESS_FINE_LOCATION) {
            when (grantResults[0]) {
                PackageManager.PERMISSION_GRANTED -> getLocation()
                PackageManager.PERMISSION_DENIED -> {}
            }
        }
    }

    companion object {
        private const val PERMISSION_REQUEST_ACCESS_FINE_LOCATION = 100
    }
}