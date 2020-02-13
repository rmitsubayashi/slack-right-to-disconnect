package com.github.rmitsubayashi.domain.serviceLocator

import com.github.rmitsubayashi.domain.interactor.HomeInteractor
import com.github.rmitsubayashi.domain.interactor.MessageInputInteractor
import com.github.rmitsubayashi.domain.interactor.SlackInteractor
import com.github.rmitsubayashi.domain.interactor.SettingsInteractor
import org.koin.dsl.module

val interactorModule = module {
    factory { HomeInteractor(get(), get(), get()) }
    factory { SlackInteractor(get()) }
    factory {
        SettingsInteractor(get())
    }
    factory {MessageInputInteractor(get())}
}