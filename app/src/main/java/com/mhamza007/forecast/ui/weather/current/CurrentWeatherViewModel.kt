package com.mhamza007.forecast.ui.weather.current

import androidx.lifecycle.ViewModel
import com.mhamza007.forecast.data.provider.UnitProvider
import com.mhamza007.forecast.data.repository.ForecastRepository
import com.mhamza007.forecast.internal.UnitSystem
import com.mhamza007.forecast.internal.lazyDeferred

class CurrentWeatherViewModel(
    private val forecastRepository: ForecastRepository,
    unitProvider: UnitProvider
) : ViewModel() {
    private val unitSystem = unitProvider.getUnitSystem()

    val isMetric: Boolean
        get() = unitSystem == UnitSystem.METRIC

    val weather by lazyDeferred {
        forecastRepository.getCurrentWeather()
    }

    val weatherLocation by lazyDeferred {
        forecastRepository.getWeatherLocation()
    }
}
