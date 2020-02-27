package com.github.rmitsubayashi.presentation.onboarding

import com.github.rmitsubayashi.presentation.BasePresenter
import com.github.rmitsubayashi.presentation.BaseView

interface BenefitsContract {
    interface View: BaseView {
        fun openURL(url: String)
        fun hideAppBar()
    }

    interface Presenter: BasePresenter {
        fun clickNext()
    }
}