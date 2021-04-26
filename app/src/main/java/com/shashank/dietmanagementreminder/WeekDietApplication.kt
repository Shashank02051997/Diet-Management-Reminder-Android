package com.shashank.dietmanagementreminder

import android.app.Application
import com.shashank.dietmanagementreminder.data.network.ApiInterface
import com.shashank.dietmanagementreminder.data.network.NetworkConnectionInterceptor
import com.shashank.dietmanagementreminder.data.repositories.WeekDietRepository
import com.shashank.dietmanagementreminder.ui.viewmodelfactory.WeekDietViewModelFactory
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton

class WeekDietApplication : Application(), KodeinAware {

    override val kodein = Kodein.lazy {
        import(androidXModule(this@WeekDietApplication))

        bind() from singleton { NetworkConnectionInterceptor(instance()) }
        bind() from singleton { ApiInterface(instance()) }
        bind() from singleton { WeekDietRepository(instance()) }
        bind() from provider { WeekDietViewModelFactory(instance()) }
    }


    override fun onCreate() {
        super.onCreate()

    }
}
