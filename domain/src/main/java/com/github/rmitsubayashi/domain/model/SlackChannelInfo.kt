package com.github.rmitsubayashi.domain.model

data class SlackChannelInfo(
    override val id: String,
    override val name: String
): Recipient