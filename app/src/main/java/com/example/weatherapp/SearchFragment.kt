package com.example.weatherapp

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.databinding.FragmentSearchBinding
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.AutocompletePrediction
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.PlacesClient

data class PlaceResult(
    val name: String,
    val address: String,
    val lat: Double,
    val long: Double
)

class PlacesAdapter(
    private val places: List<PlaceResult>,
    private val onItemClick: (PlaceResult) -> Unit,
) : RecyclerView.Adapter<PlacesAdapter.PlaceViewHolder>() {
    inner class PlaceViewHolder(val textView: TextView) : RecyclerView.ViewHolder(textView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaceViewHolder {
        val textView = TextView(parent.context).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            text = "Hello"
            textSize = 16f
            setPadding(16, 16, 16, 16)
        }
        return PlaceViewHolder(textView)
    }

    override fun onBindViewHolder(holder: PlaceViewHolder, position: Int) {
        holder.textView.text = places[position].name + ", " + places[position].address
        holder.textView.setOnClickListener(object: View.OnClickListener {
            override fun onClick(v: View?) {
                onItemClick(places[position])
            }
        })
    }

    override fun getItemCount(): Int = places.size
}

class SearchFragment : Fragment() {
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private lateinit var placesClient: PlacesClient
    private val placesList = mutableListOf<PlaceResult>()
    private lateinit var adapter: PlacesAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Places.initialize(activity, getResources().getString(R.string.api_key))
        placesClient = Places.createClient(activity)

        // Setup RecyclerView
        val recyclerView: RecyclerView = binding.searchRecyclerview
        adapter = PlacesAdapter(placesList) { place ->
            (activity as MainActivity?)?.gotoSearchToDashboard(place.lat, place.long)
        }
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = adapter

        binding.searchTextinput2.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                val query = s.toString()
                if (query.length > 2) { // Trigger search after 3 characters
                    searchPlaces(query)
                }
            }
        })
    }

    private fun searchPlaces(query: String) {
        val request = FindAutocompletePredictionsRequest.builder()
            .setQuery(query)
            .build()

        placesClient.findAutocompletePredictions(request)
            .addOnSuccessListener { response ->
                placesList.clear()
                for (prediction: AutocompletePrediction in response.autocompletePredictions) {
                    Log.e("Places", prediction.toString())

                    val placeFields = listOf(Place.Field.LAT_LNG)
                    val request2 = FetchPlaceRequest.newInstance(prediction.placeId, placeFields)

                    // Fetch the place details
                    placesClient.fetchPlace(request2).addOnSuccessListener { fetchPlaceResponse ->
                        val place = fetchPlaceResponse.place
                        val latLng = place.latLng
                        if (latLng != null) {
                            val latitude = latLng.latitude
                            val longitude = latLng.longitude
                            placesList.add(
                                PlaceResult(
                                    name = prediction.getPrimaryText(null).toString(),
                                    address = prediction.getSecondaryText(null).toString(),
                                    lat = latitude,
                                    long = longitude
                                )
                            )
                        }
                    }.addOnFailureListener { exception ->
                        Log.e("PlaceDetails", "Place not found: ${exception.message}")
                    }.addOnSuccessListener {
                        adapter.notifyDataSetChanged()
                    }
                }
            }
            .addOnFailureListener { exception ->
                Log.e("PlacesAPI", "Error: ${exception.message}", exception)
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}