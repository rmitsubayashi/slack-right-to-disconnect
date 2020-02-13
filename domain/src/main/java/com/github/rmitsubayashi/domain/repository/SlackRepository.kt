package com.github.rmitsubayashi.domain.repository

import com.github.rmitsubayashi.domain.Resource
import com.github.rmitsubayashi.domain.model.*

interface SlackRepository {
    suspend fun getSlackToken(): Resource<SlackTokenInfo>
    suspend fun setSlackToken(slackTokenInfo: SlackTokenInfo): Resource<Unit>
    suspend fun validateSlackToken(slackToken: SlackToken): Resource<SlackTokenInfo>
    suspend fun post(
        message: Message,
        id: String,
        threadID: String? = null
    ): Resource<String>

    suspend fun getSlackChannels(): Resource<List<SlackChannelInfo>>
    suspend fun getUsers(): Resource<List<UserInfo>>
    suspend fun getRecentThreads(): Resource<List<ThreadInfo>>
    fun saveThreadInfo(id: String, message: Message, threadID: String)
    fun updateRecentThreads(recentThreads: List<ThreadInfo>)


}