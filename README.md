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

## How to run the Project

* Get the zip file from GitHub.
* Add the project to Android Studio. A USB connection can be used to connect an Android phone to a computer and run the app that way, or it can be run through an emulator.
* A few changes need to be made in order for an Android phone to work. This is how the settings are set:
      -> Make sure your device is in developer mode.
      -> Allow debugging over USB.
* Once the phone is linked, the Android Studio needs to detect it. If your Android phone has been found by Android Studio, click the "Run" button.
