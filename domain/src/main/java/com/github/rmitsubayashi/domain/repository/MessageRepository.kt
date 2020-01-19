package com.github.rmitsubayashi.domain.repository

import com.github.rmitsubayashi.domain.Resource
import com.github.rmitsubayashi.domain.model.MessageTemplate

interface MessageRepository {
    suspend fun getMessageTemplates(): Resource<List<MessageTemplate>>
    suspend fun updateMessageTemplates(messageTemplates: List<MessageTemplate>): Resource<Unit>
}