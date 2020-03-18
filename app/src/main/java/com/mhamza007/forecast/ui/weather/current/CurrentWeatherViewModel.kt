package com.mhamza007.forecast.ui.weather.current

import androidx.lifecycle.ViewModel
import com.mhamza007.forecast.data.repository.ForecastRepository
import com.mhamza007.forecast.internal.UnitSystem
import com.mhamza007.forecast.internal.lazyDeferred

class CurrentWeatherViewModel(
    private val forecastRepository: ForecastRepository
) : ViewModel() {
    private val unitSystem = UnitSystem.METRIC

    val isMetric: Boolean
        get() = unitSystem == UnitSystem.METRIC

    val weather by lazyDeferred {
        forecastRepository.getCurrentWeather()
    }
}
