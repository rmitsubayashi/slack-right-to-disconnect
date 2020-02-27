package com.github.rmitsubayashi.domain.serviceLocator

import com.github.rmitsubayashi.domain.interactor.*
import org.koin.dsl.module

val interactorModule = module {
    factory { PostInteractor(get(), get()) }
    factory {
        SettingsInteractor(get())
    }
    factory {MessageInputInteractor(get())}
    factory {SelectChannelInteractor(get())}
    factory {SelectUserInteractor(get(), get())}
    factory {SelectRecentThreadInteractor(get())}
    factory { BookmarkInteractor(get()) }
    factory { OnboardingInteractor(get()) }
    factory { AuthenticationInteractor(get(), get()) }
}