package io.jeagertracing.exampletracingapp

import android.app.Application
import io.jeagertracing.exampletracingapp.di.tracingModule
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.koin.android.ext.android.startKoin

class KoinApp : Application() {

    override fun onCreate() {
        super.onCreate()

        GlobalScope.launch {
            startKoin(
                this@KoinApp,
                listOf(tracingModule)
            )
        }

    }

}
