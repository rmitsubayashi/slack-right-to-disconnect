package com.github.rmitsubayashi.presentation.onboarding

import com.github.rmitsubayashi.presentation.BasePresenter
import com.github.rmitsubayashi.presentation.BaseView

interface LoginRetryContract {
    interface View: BaseView {
        fun openURL(url: String)
        fun hideAppBar()
        fun getSlackClientID(): String
    }

    interface Presenter: BasePresenter {
        fun clickRetry()
    }
}