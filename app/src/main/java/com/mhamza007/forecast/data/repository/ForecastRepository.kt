package com.mhamza007.forecast.data.repository

import androidx.lifecycle.LiveData
import com.mhamza007.forecast.data.db.entity.CurrentWeatherEntry
import com.mhamza007.forecast.data.db.entity.WeatherLocation

interface ForecastRepository {
    suspend fun getCurrentWeather(): LiveData<CurrentWeatherEntry>
    suspend fun getWeatherLocation(): LiveData<WeatherLocation>
}