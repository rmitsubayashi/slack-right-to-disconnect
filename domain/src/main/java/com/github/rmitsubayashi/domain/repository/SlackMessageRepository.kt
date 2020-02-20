package com.github.rmitsubayashi.domain.repository

import com.github.rmitsubayashi.domain.Resource
import com.github.rmitsubayashi.domain.model.Message

interface SlackMessageRepository {
    suspend fun post(message: Message): Resource<String>
    suspend fun getRecentThreads(): Resource<List<Message>>
    fun saveRecentThread(message: Message)
    fun updateRecentThreads(recentThreads: List<Message>)
}