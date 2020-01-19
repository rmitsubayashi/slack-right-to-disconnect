package com.github.rmitsubayashi.data.repository

import android.content.SharedPreferences
import androidx.core.content.edit
import com.github.rmitsubayashi.domain.Resource
import com.github.rmitsubayashi.domain.model.MessageTemplate
import com.github.rmitsubayashi.domain.repository.MessageRepository

internal class MessageDataRepository(private val sharedPreferences: SharedPreferences): MessageRepository {

    override suspend fun getMessageTemplates(): Resource<List<MessageTemplate>> {
        val messageTemplateStrings = sharedPreferences.getStringList(SharedPrefsKeys.MESSAGE_TEMPLATE) ?: return Resource.success(emptyList())
        val messageTemplates = messageTemplateStrings.map { MessageTemplate(it) }
        return Resource.success(messageTemplates)
    }

    override suspend fun updateMessageTemplates(messageTemplates: List<MessageTemplate>): Resource<Unit> {
        val messageTemplateStrings = messageTemplates.map { it.value }
        sharedPreferences.edit { putStringList(SharedPrefsKeys.MESSAGE_TEMPLATE, messageTemplateStrings)}
        return Resource.success(null)
    }

}