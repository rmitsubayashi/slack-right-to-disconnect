package com.github.rmitsubayashi.slackrighttodisconnect

import android.app.Application
import com.github.rmitsubayashi.data.serviceLocator.repositoryModule
import com.github.rmitsubayashi.domain.serviceLocator.interactorModule
import com.github.rmitsubayashi.slackrighttodisconnect.serviceLocator.presenterModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

@Suppress("unused")
class R2DApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        initServiceLocator()
    }

    private fun initServiceLocator() {
        startKoin {
            androidContext(this@R2DApplication)
            modules(listOf(repositoryModule, presenterModule, interactorModule))
        }
    }
}