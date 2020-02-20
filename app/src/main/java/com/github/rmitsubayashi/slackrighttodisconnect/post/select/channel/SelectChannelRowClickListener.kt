package com.github.rmitsubayashi.slackrighttodisconnect.post.select.channel

import com.github.rmitsubayashi.domain.model.Recipient

interface SelectChannelRowClickListener {
    fun onChannelClicked(channel: Recipient)
}