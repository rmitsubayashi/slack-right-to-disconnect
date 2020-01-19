package com.github.rmitsubayashi.data.model

import com.github.rmitsubayashi.domain.model.SlackToken

class SlackAuthToken(private val slackToken: SlackToken) {
    override fun toString() = "Bearer ${slackToken.value}"
}