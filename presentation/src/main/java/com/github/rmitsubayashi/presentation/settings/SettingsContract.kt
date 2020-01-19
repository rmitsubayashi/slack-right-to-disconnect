package com.github.rmitsubayashi.presentation.settings

import com.github.rmitsubayashi.domain.model.*
import com.github.rmitsubayashi.presentation.BasePresenter
import com.github.rmitsubayashi.presentation.BaseView

interface SettingsContract {
    interface View: BaseView {
        fun showMessageTemplateSelector()
        fun showInvalidSlackToken()
        fun showInvalidSlackChannel()
        fun updateSlackChannelSettingSummary(channel: SlackChannel?)
        fun updateSlackChannelList(channelNames: List<SlackChannel>?)
        fun updateSlackTokenSettingSummary(isSet: Boolean)
    }

    interface Presenter: BasePresenter {
        fun saveSlackToken(slackToken: SlackToken)
        fun saveSlackChannel(slackChannelID: SlackChannelID)
        fun checkCurrentSlackChannel()
        fun checkCurrentSlackToken()
        fun loadSlackChannels()
    }
}