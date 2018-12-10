package io.jeagertracing.exampletracingapp

import android.app.Application
import io.jeagertracing.exampletracingapp.di.tracingModule
import org.koin.android.ext.android.startKoin

class KoinApp : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin(
            this,
            listOf(tracingModule)
        )
    }

}
