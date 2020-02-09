package com.github.rmitsubayashi.presentation.settings

import com.github.rmitsubayashi.domain.model.*
import com.github.rmitsubayashi.presentation.BasePresenter
import com.github.rmitsubayashi.presentation.BaseView

interface SettingsContract {
    interface View: BaseView {
        fun showInvalidSlackToken()
        fun showInvalidSlackChannel()
        fun updateSlackChannelSettingSummary(channelInfo: SlackChannelInfo?)
        fun updateSlackChannelList(channelNames: List<SlackChannelInfo>?)
        fun updateSlackTokenSettingSummary(slackTokenInfo: SlackTokenInfo?)
    }

    interface Presenter: BasePresenter {
        fun saveSlackToken(slackToken: SlackToken)
        fun saveSlackChannel(slackChannelID: String)
        fun checkCurrentSlackChannel()
        fun checkCurrentSlackToken()
        fun loadSlackChannels()
    }
}