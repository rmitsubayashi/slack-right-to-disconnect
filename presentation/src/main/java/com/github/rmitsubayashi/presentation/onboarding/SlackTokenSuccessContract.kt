package com.github.rmitsubayashi.presentation.onboarding

import com.github.rmitsubayashi.presentation.BasePresenter
import com.github.rmitsubayashi.presentation.BaseView

interface SlackTokenSuccessContract {
    interface View: BaseView {
        fun navigateToSelectType()
    }

    interface Presenter: BasePresenter {
        fun clickFinish()
    }
}