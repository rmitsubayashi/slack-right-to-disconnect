package com.github.rmitsubayashi.slackrighttodisconnect.post

import com.github.rmitsubayashi.domain.model.Recipient

interface PostRecipientRowClickListener {
    fun onItemClicked(recipient: Recipient)
}