package com.github.rmitsubayashi.presentation.onboarding

import com.github.rmitsubayashi.presentation.BasePresenter
import com.github.rmitsubayashi.presentation.BaseView

interface BenefitsContract {
    interface View: BaseView {
        fun navigateToSlackToken()
        fun hideAppBar()
    }

    interface Presenter: BasePresenter {
        fun clickNext()
    }
}