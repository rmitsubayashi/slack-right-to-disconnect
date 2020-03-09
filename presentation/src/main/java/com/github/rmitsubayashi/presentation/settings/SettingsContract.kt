package com.github.rmitsubayashi.presentation.settings

import com.github.rmitsubayashi.domain.model.SlackToken
import com.github.rmitsubayashi.domain.model.SlackTokenInfo
import com.github.rmitsubayashi.presentation.BasePresenter
import com.github.rmitsubayashi.presentation.BaseView

interface SettingsContract {
    interface View: BaseView {
        fun showInvalidSlackToken()
        fun showSaved()
        fun updateSlackTokenSettingSummary(slackTokenInfo: SlackTokenInfo?)
    }

    interface Presenter: BasePresenter {
        fun saveSlackToken(slackToken: SlackToken)
        fun checkCurrentSlackToken()
    }
}