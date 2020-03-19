package com.mhamza007.forecast.data.provider

import com.mhamza007.forecast.internal.UnitSystem

interface UnitProvider {
    fun getUnitSystem(): UnitSystem
}