package com.app.freebook.openlib

import android.app.Application
import com.app.freebook.core.di.*
import com.app.freebook.openlib.di.useCaseModule
import com.app.freebook.openlib.di.viewModeModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger(Level.NONE)
            androidContext(this@App)
            modules(
                listOf(
                    networkModule,
                    roomModule,
                    repositoryModule,
                    useCaseModule,
                    viewModeModule
                )
            )
        }
    }
}