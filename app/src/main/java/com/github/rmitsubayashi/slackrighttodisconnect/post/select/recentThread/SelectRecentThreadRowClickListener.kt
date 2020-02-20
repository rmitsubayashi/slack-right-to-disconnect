package com.github.rmitsubayashi.slackrighttodisconnect.post.select.recentThread

import com.github.rmitsubayashi.domain.model.Message

interface SelectRecentThreadRowClickListener {
    fun onRecentThreadClicked(message: Message)
}