package com.github.rmitsubayashi.slackrighttodisconnect.post.select.user

import com.github.rmitsubayashi.domain.model.Recipient

interface SelectUserRowClickListener {
    fun onUserClicked(user: Recipient, selected: Boolean)
}