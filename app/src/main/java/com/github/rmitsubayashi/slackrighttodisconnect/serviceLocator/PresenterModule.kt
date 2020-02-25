package com.github.rmitsubayashi.slackrighttodisconnect.serviceLocator

import com.github.rmitsubayashi.presentation.onboarding.*
import com.github.rmitsubayashi.presentation.post.*
import com.github.rmitsubayashi.presentation.settings.SettingsContract
import com.github.rmitsubayashi.presentation.settings.SettingsPresenter
import com.github.rmitsubayashi.slackrighttodisconnect.onboarding.BenefitsFragment
import com.github.rmitsubayashi.slackrighttodisconnect.onboarding.SlackTokenFragment
import com.github.rmitsubayashi.slackrighttodisconnect.onboarding.SlackTokenSuccessFragment
import com.github.rmitsubayashi.slackrighttodisconnect.post.post.PostFragment
import com.github.rmitsubayashi.slackrighttodisconnect.post.postSuccess.PostSuccessFragment
import com.github.rmitsubayashi.slackrighttodisconnect.post.select.channel.SelectChannelFragment
import com.github.rmitsubayashi.slackrighttodisconnect.post.select.recentThread.SelectRecentThreadFragment
import com.github.rmitsubayashi.slackrighttodisconnect.post.select.user.SelectUserFragment
import com.github.rmitsubayashi.slackrighttodisconnect.post.selectType.SelectTypeFragment
import com.github.rmitsubayashi.slackrighttodisconnect.settings.SettingsFragment
import org.koin.dsl.module

val presenterModule = module {
    factory<PostContract.Presenter> { (fragment: PostFragment) ->
        PostPresenter(fragment, get())
    }

    factory<SelectTypeContract.Presenter> { (fragment: SelectTypeFragment) ->
        SelectTypePresenter(fragment, get(), get())
    }

    factory<SelectRecentThreadContract.Presenter> { (fragment: SelectRecentThreadFragment) ->
        SelectRecentThreadPresenter(fragment, get())
    }

    factory<SelectUserContract.Presenter> { (fragment: SelectUserFragment) ->
        SelectUserPresenter(fragment, get())
    }

    factory<SelectChannelContract.Presenter> { (fragment: SelectChannelFragment) ->
        SelectChannelPresenter(fragment, get())
    }

    factory<SettingsContract.Presenter> { (fragment: SettingsFragment) ->
        SettingsPresenter(fragment, get())
    }

    factory<PostSuccessContract.Presenter> { (fragment: PostSuccessFragment) ->
        PostSuccessPresenter(fragment, get())
    }

    factory<BenefitsContract.Presenter> {(fragment: BenefitsFragment) ->
        BenefitsPresenter(fragment)
    }

    factory<SlackTokenContract.Presenter> {(fragment: SlackTokenFragment) ->
        SlackTokenPresenter(fragment, get(), get())
    }

    factory<SlackTokenSuccessContract.Presenter> {(fragment: SlackTokenSuccessFragment) ->
        SlackTokenSuccessPresenter(fragment)

    }
}