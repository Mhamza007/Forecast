package com.mhamza007.forecast.ui.weather.current

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.mhamza007.forecast.R
import com.mhamza007.forecast.internal.glide.GlideApp
import com.mhamza007.forecast.ui.base.ScopedFragment
import kotlinx.android.synthetic.main.current_weather_fragment.*
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance
import java.text.DecimalFormat

class CurrentWeatherFragment : ScopedFragment(), KodeinAware {

    override val kodein by closestKodein()

    private val viewModelFactory: CurrentWeatherViewModelFactory by instance()

    private lateinit var viewModel: CurrentWeatherViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.current_weather_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel =
            ViewModelProvider(this, viewModelFactory).get(CurrentWeatherViewModel::class.java)

        bindUI()
    }

    @SuppressLint("FragmentLiveDataObserve")
    private fun bindUI() = launch {
        val currentWeather = viewModel.weather.await()

        val weatherLocation = viewModel.weatherLocation.await()

        weatherLocation.observe(this@CurrentWeatherFragment, Observer { location ->
            if (location == null) return@Observer

            Log.d("location.name", location.name)
            Log.d("location.timezoneId", location.timezoneId)
            Log.d("location.country", location.country)
            Log.d("location.localtime", location.localtime)
            Log.d("location.region", location.region)
            Log.d("location.utcOffset", location.utcOffset)
            Log.d("location.id", "${location.id}")
            Log.d("location.lat", "${location.lat}")
            Log.d("location.localtimeEpoch", "${location.localtimeEpoch}")
            Log.d("location.lon", "${location.lon}")
            Log.d("location.zonedDateTime", "${location.zonedDateTime}")

            updateLocation(location.name)
        })

        currentWeather.observe(this@CurrentWeatherFragment, Observer {
            if (it == null) return@Observer

            group_loading.visibility = View.GONE
            updateDateToToday()

            updateTemperatures(it.temperature, it.feelslike)
            updateCondition(it.weatherDescriptions[0])
            updatePrecipitation(it.precip)
            updateWind(it.windDir, it.windSpeed)
            updateVisibility(it.visibility)

            GlideApp.with(this@CurrentWeatherFragment)
                .load(it.weatherIcons[0])
                .into(imageView_condition_icon)
        })
    }

    private fun chooseLocalizedUnitAbbreviation(metric: String, imperial: String): String {
        return if (viewModel.isMetric) metric else imperial

    }

    private fun updateLocation(location: String) {
        (activity as? AppCompatActivity)?.supportActionBar?.title = location
    }

    private fun updateDateToToday() {
        (activity as? AppCompatActivity)?.supportActionBar?.subtitle = "Today"
    }

    private fun updateTemperatures(temperature: Double, feelsLikeTemp: Double) {
        val unitAbbreviation = chooseLocalizedUnitAbbreviation("°C", "°F")
        if (unitAbbreviation == "°C") {
            textView_temperature.text = "$temperature$unitAbbreviation"
            textView_feels_like_temperature.text = "Feels Like $feelsLikeTemp$unitAbbreviation"
        } else if (unitAbbreviation == "°F") {
            val tempInImperial = (temperature * 9 / 5) + 32
            textView_temperature.text = "${decimalFormat.format(tempInImperial)}$unitAbbreviation"
            textView_feels_like_temperature.text =
                "Feels Like ${(feelsLikeTemp * 9 / 5) + 32}$unitAbbreviation"
        }
    }

    private fun updateCondition(condition: String) {
        textView_condition.text = condition
    }

    private fun updatePrecipitation(precipitationVolume: Double) {
        val unitAbbreviation = chooseLocalizedUnitAbbreviation("mm", "in")
        if (unitAbbreviation == "mm") {
            textView_precipitation.text = "Precipitation: $precipitationVolume $unitAbbreviation"
        } else if (unitAbbreviation == "in") {
            val precipitationVolumeImperial = precipitationVolume / 25.4
            textView_precipitation.text =
                "Precipitation: ${decimalFormat.format(precipitationVolumeImperial)} $unitAbbreviation"
        }
    }

    private fun updateWind(windDirection: String, windSpeed: Double) {
        val unitAbbreviation = chooseLocalizedUnitAbbreviation("kph", "mph")
        if (unitAbbreviation == "kph") {
            textView_wind.text = "Wind: $windDirection, $windSpeed $unitAbbreviation"
        } else if (unitAbbreviation == "mph") {
            val windSpeedImperial = windSpeed / 1.609
            textView_wind.text =
                "Wind: $windDirection, ${decimalFormat.format(windSpeedImperial)} $unitAbbreviation"
        }
    }

    private fun updateVisibility(visibilityDistance: Double) {
        val unitAbbreviation = chooseLocalizedUnitAbbreviation("km", "mi.")
        if (unitAbbreviation == "km") {
            textView_visibility.text = "Visibility: $visibilityDistance $unitAbbreviation"
        } else if (unitAbbreviation == "mi.") {
            val visibilityDistanceImperial = visibilityDistance / 1.609
            textView_visibility.text =
                "Visibility: ${decimalFormat.format(visibilityDistanceImperial)} $unitAbbreviation"
        }
    }

    companion object {
        val decimalFormat = DecimalFormat("0.00")
    }
}
