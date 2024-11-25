# WeatherApp

Weatherapp is a simple weather forecast app, which uses OpenWeather API to fetch 5 day/3 hour step forecast data and to fetch places, cities, counties, coords etc. from Google Map API. The goal of this app is to get the weather of a particular place over a span of 5 days with 3 hour step.

## Libraries and Tools 🛠

<li><a href="https://github.com/lopspower/RxAnimation">RxAnimation</a></li>
<li><a href="https://github.com/ReactiveX/RxJava">Rxjava</a></li>
<li><a href="https://github.com/AhsanMunir/Google-Places-SDK-for-Android">Google Places</a></li>
<li><a href="https://github.com/square/retrofit">Retrofit</a></li>
<li><a href="https://github.com/square/retrofit/blob/trunk/retrofit-converters/gson/README.md">Retrofit GSON</a></li>

## API

<li><a href="https://openweathermap.org/forecast5">Openweather 5 day weather forecase</a></li>

## Design

* On app open, from android LocationManager, fetch the current location and call the openweather api, to fetch the current weather and display in the recyclerview.
* On search fragment, on any text changes, fetch the location based on the query.
* On tap on place, use the place to fetch lat and long, use the location to fetch the weather and show the same in recyclerview.
