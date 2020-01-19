package com.github.rmitsubayashi.slackrighttodisconnect.serviceLocator

import com.github.rmitsubayashi.presentation.home.HomeContract
import com.github.rmitsubayashi.presentation.home.HomePresenter
import com.github.rmitsubayashi.presentation.settings.MessageTemplateContract
import com.github.rmitsubayashi.presentation.settings.MessageTemplatePresenter
import com.github.rmitsubayashi.presentation.settings.SettingsContract
import com.github.rmitsubayashi.presentation.settings.SettingsPresenter
import com.github.rmitsubayashi.slackrighttodisconnect.home.HomeActivity
import com.github.rmitsubayashi.slackrighttodisconnect.settings.MessageTemplateActivity
import com.github.rmitsubayashi.slackrighttodisconnect.settings.SettingsActivity
import com.github.rmitsubayashi.slackrighttodisconnect.settings.SettingsFragment
import org.koin.dsl.module

val presenterModule = module {
    factory<HomeContract.Presenter> { (activity: HomeActivity) ->
        HomePresenter(activity, get())
    }
    factory<SettingsContract.Presenter> { (fragment: SettingsFragment) ->
        SettingsPresenter(fragment, get())
    }
    factory<MessageTemplateContract.Presenter> { (activity: MessageTemplateActivity) ->
        MessageTemplatePresenter(activity, get())
    }
}