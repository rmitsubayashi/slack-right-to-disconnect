package com.github.rmitsubayashi.slackrighttodisconnect.serviceLocator

import com.github.rmitsubayashi.domain.logging.OnboardingLogs
import com.github.rmitsubayashi.domain.logging.PostLogs
import com.github.rmitsubayashi.slackrighttodisconnect.logs.FirebaseOnboardingLogs
import com.github.rmitsubayashi.slackrighttodisconnect.logs.FirebasePostLogs
import com.google.firebase.analytics.FirebaseAnalytics
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val loggingModule = module {
    single { FirebaseAnalytics.getInstance(androidContext()) }
    factory<OnboardingLogs> { FirebaseOnboardingLogs(get()) }
    factory<PostLogs> { FirebasePostLogs(get()) }
}