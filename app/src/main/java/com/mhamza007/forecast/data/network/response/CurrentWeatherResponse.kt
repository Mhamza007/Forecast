package com.mhamza007.forecast.data.network.response

import com.google.gson.annotations.SerializedName
import com.mhamza007.forecast.data.db.entity.CurrentWeatherEntry
import com.mhamza007.forecast.data.db.entity.Location
import com.mhamza007.forecast.data.db.entity.Request

data class CurrentWeatherResponse(
    val request: Request,
    val location: Location,
    @SerializedName("current")
    val currentWeatherEntry: CurrentWeatherEntry
)