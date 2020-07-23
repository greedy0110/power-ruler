package com.greedy0110.powerruler

import android.app.Application
import timber.log.Timber

class PowerApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())
    }
}