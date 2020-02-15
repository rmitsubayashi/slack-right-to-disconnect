package com.github.rmitsubayashi.slackrighttodisconnect.post.selectPostReipientType

import com.github.rmitsubayashi.domain.model.RecipientType

interface HeaderListener {
    fun onRecipientTypeClicked(type: RecipientType)
}