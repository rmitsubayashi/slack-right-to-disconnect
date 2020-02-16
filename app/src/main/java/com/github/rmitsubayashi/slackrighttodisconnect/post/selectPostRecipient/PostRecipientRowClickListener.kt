package com.github.rmitsubayashi.slackrighttodisconnect.post.selectPostRecipient

import com.github.rmitsubayashi.domain.model.Recipient
import com.github.rmitsubayashi.domain.model.UserInfo

interface PostRecipientRowClickListener {
    fun onItemClicked(recipient: Recipient)
    fun onUserItemClicked(user: UserInfo, selected: Boolean)
}