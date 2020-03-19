package com.mhamza007.forecast

import android.app.Application
import android.content.Context
import androidx.preference.PreferenceManager
import com.google.android.gms.location.LocationServices
import com.jakewharton.threetenabp.AndroidThreeTen
import com.mhamza007.forecast.data.db.database.ForecastDatabase
import com.mhamza007.forecast.data.network.*
import com.mhamza007.forecast.data.provider.LocationProvider
import com.mhamza007.forecast.data.provider.LocationProviderImpl
import com.mhamza007.forecast.data.provider.UnitProvider
import com.mhamza007.forecast.data.provider.UnitProviderImpl
import com.mhamza007.forecast.data.repository.ForecastRepository
import com.mhamza007.forecast.data.repository.ForecastRepositoryImpl
import com.mhamza007.forecast.ui.weather.current.CurrentWeatherViewModelFactory
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton

class ForecastApplication : Application(), KodeinAware {
    override val kodein = Kodein.lazy {
        import(androidXModule(this@ForecastApplication))

        bind() from singleton {
            ForecastDatabase(instance())
        }
        bind() from singleton {
            instance<ForecastDatabase>().currentWeatherDao()
        }
        bind() from singleton {
            instance<ForecastDatabase>().weatherLocationDao()
        }
        bind<ConnectivityInterceptor>() with singleton {
            ConnectivityInterceptorImpl(instance())
        }
        bind() from singleton {
            WeatherStackApiService(instance())
        }
        bind<WeatherNetworkDataSource>() with singleton {
            WeatherNetworkDataSourceImpl(instance())
        }
        bind() from provider {
            LocationServices.getFusedLocationProviderClient(instance<Context>())
        }
        bind<LocationProvider>() with singleton {
            LocationProviderImpl(instance(), instance())
        }
        bind<ForecastRepository>() with singleton {
            ForecastRepositoryImpl(instance(), instance(), instance(), instance())
        }
        bind<UnitProvider>() with singleton {
            UnitProviderImpl(instance())
        }
        bind() from provider {
            CurrentWeatherViewModelFactory(instance(), instance())
        }
    }

    override fun onCreate() {
        super.onCreate()

        AndroidThreeTen.init(this)
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false)
    }
}