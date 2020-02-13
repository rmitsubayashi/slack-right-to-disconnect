package com.github.rmitsubayashi.slackrighttodisconnect.serviceLocator

import com.github.rmitsubayashi.presentation.post.*
import com.github.rmitsubayashi.presentation.settings.SettingsContract
import com.github.rmitsubayashi.presentation.settings.SettingsPresenter
import com.github.rmitsubayashi.slackrighttodisconnect.post.PostFragment
import com.github.rmitsubayashi.slackrighttodisconnect.post.PostSuccessFragment
import com.github.rmitsubayashi.slackrighttodisconnect.post.SelectPostRecipientFragment
import com.github.rmitsubayashi.slackrighttodisconnect.post.SelectPostRecipientTypeFragment
import com.github.rmitsubayashi.slackrighttodisconnect.settings.SettingsFragment
import org.koin.dsl.module

val presenterModule = module {
    factory<PostContract.Presenter> { (fragment: PostFragment) ->
        PostPresenter(fragment, get())
    }
    factory<SelectPostRecipientTypeContract.Presenter> { (fragment: SelectPostRecipientTypeFragment) ->
        SelectRecipientTypePresenter(fragment, get())
    }
    factory<SelectPostRecipientContract.Presenter> { (fragment: SelectPostRecipientFragment) ->
        SelectPostRecipientPresenter(fragment, get())
    }
    factory<SettingsContract.Presenter> { (fragment: SettingsFragment) ->
        SettingsPresenter(fragment, get())
    }

    factory<PostSuccessContract.Presenter> { (fragment: PostSuccessFragment) ->
        PostSuccessPresenter(fragment, get())
    }
}