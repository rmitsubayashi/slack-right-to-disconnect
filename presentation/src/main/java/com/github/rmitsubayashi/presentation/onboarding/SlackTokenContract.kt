package com.github.rmitsubayashi.presentation.onboarding

import com.github.rmitsubayashi.presentation.BasePresenter
import com.github.rmitsubayashi.presentation.BaseView

interface SlackTokenContract {
    interface View: BaseView {
        fun navigateToSlackTokenSuccess()
        fun showValidationError()
        fun showNoNetwork()
    }

    interface Presenter: BasePresenter {
        fun verifyToken(input: String)
    }
}