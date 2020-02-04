package com.github.rmitsubayashi.slackrighttodisconnect.serviceLocator

import com.github.rmitsubayashi.presentation.home.HomeContract
import com.github.rmitsubayashi.presentation.home.HomePresenter
import com.github.rmitsubayashi.presentation.settings.SettingsContract
import com.github.rmitsubayashi.presentation.settings.SettingsPresenter
import com.github.rmitsubayashi.slackrighttodisconnect.home.HomeActivity
import com.github.rmitsubayashi.slackrighttodisconnect.settings.SettingsFragment
import org.koin.dsl.module

val presenterModule = module {
    factory<HomeContract.Presenter> { (activity: HomeActivity) ->
        HomePresenter(activity, get())
    }
    factory<SettingsContract.Presenter> { (fragment: SettingsFragment) ->
        SettingsPresenter(fragment, get())
    }
}