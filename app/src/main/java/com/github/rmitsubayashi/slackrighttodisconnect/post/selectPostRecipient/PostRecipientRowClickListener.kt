package com.github.rmitsubayashi.slackrighttodisconnect.post.selectPostRecipient

import com.github.rmitsubayashi.domain.model.Recipient

interface PostRecipientRowClickListener {
    fun onItemClicked(recipient: Recipient)
}