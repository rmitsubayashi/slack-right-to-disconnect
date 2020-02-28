package com.github.rmitsubayashi.presentation.onboarding

import com.github.rmitsubayashi.presentation.BasePresenter
import com.github.rmitsubayashi.presentation.BaseView

interface SlackLoginResultContract {
    interface View: BaseView {
        fun navigateToSelectType()
        fun showLoading(loading: Boolean)
        fun showSuccess()
        fun hideAppbar()
        fun showAppbar()
    }

    interface Presenter: BasePresenter {
        fun clickFinish()
        fun receiveLoginResult(code: String?, state: String?)
    }
}