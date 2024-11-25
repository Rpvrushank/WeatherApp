package com.example.weatherapp

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.api.Main
import com.example.weatherapp.api.WeatherData
import com.example.weatherapp.databinding.FragmentDashboardBinding

class WeatherAdapter(private val weatherList: List<WeatherData>) :
    RecyclerView.Adapter<WeatherAdapter.WeatherViewHolder>() {

    class WeatherViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val weatherIcon: ImageView = itemView.findViewById(R.id.weatherIcon)
        val weatherDate: TextView = itemView.findViewById(R.id.weatherDate)
        val weatherTemp: TextView = itemView.findViewById(R.id.weatherTemp)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_weather_card, parent, false)
        return WeatherViewHolder(view)
    }

    override fun onBindViewHolder(holder: WeatherViewHolder, position: Int) {
        val weatherData = weatherList[position]

        // Set Date
        holder.weatherDate.text = weatherData.dt_txt

        // Convert Temperature to Celsius
        val temperatureCelsius = weatherData.main.temp - 273.15
        holder.weatherTemp.text = String.format("%.1f°C", temperatureCelsius)

        // Set Weather Icon
        val iconRes = when (weatherData.weather[0].icon) {
            "01d" -> R.drawable.a01d_svg
            "01n" -> R.drawable.a01n_svg
            "02d" -> R.drawable.a02d_svg
            "02n" -> R.drawable.a02n_svg
            "03d" -> R.drawable.a03d_svg
            "03n" -> R.drawable.a03n_svg
            "04d" -> R.drawable.a04d_svg
            "04n" -> R.drawable.a04n_svg
            "09d" -> R.drawable.a09d_svg
            "09n" -> R.drawable.a09n_svg
            "10d" -> R.drawable.a10d_svg
            "10n" -> R.drawable.a10n_svg
            "11d" -> R.drawable.a11d_svg
            "11n" -> R.drawable.a11n_svg
            "1232n" -> R.drawable.a1232n_svg
            "13d" -> R.drawable.a13d_svg
            "13n" -> R.drawable.a13n_svg
            "50d" -> R.drawable.a50d_svg
            "50n" -> R.drawable.a50n_svg
            else -> R.drawable.a50n_svg
        }
        holder.weatherIcon.setImageResource(iconRes)
    }

    override fun getItemCount(): Int = weatherList.size
}

class DashboardFragment : Fragment() {
    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!
    private lateinit var recyclerView: RecyclerView
    private lateinit var weatherAdapter: WeatherAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.e("ASDF", (activity as MainActivity?)?.lastWeatherResponse.toString())
        (activity as MainActivity?)?.lastWeatherResponse?.let { setResponse(it) }
    }

    fun setResponse(weatherData: List<WeatherData>) {
        recyclerView = binding.dashboardRecyclerView
        recyclerView.layoutManager = LinearLayoutManager(activity)
        weatherAdapter = WeatherAdapter(weatherData)
        recyclerView.adapter = weatherAdapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}