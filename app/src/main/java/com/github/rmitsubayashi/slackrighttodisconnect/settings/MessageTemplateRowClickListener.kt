package com.github.rmitsubayashi.slackrighttodisconnect.settings

import com.github.rmitsubayashi.domain.model.MessageTemplate

interface MessageTemplateRowClickListener {
    fun onItemClicked(messageTemplate: MessageTemplate)
}