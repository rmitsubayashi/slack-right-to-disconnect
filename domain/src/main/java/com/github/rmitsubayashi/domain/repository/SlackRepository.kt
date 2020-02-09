package com.github.rmitsubayashi.domain.repository

import com.github.rmitsubayashi.domain.Resource
import com.github.rmitsubayashi.domain.model.*

interface SlackRepository {
    suspend fun getSlackToken(): Resource<SlackTokenInfo>
    suspend fun setSlackToken(slackTokenInfo: SlackTokenInfo): Resource<Unit>
    suspend fun validateSlackToken(slackToken: SlackToken): Resource<SlackTokenInfo>
    suspend fun post(message: Message, channelID: String, token: SlackToken): Resource<Unit>
    suspend fun getSlackChannels(token: SlackToken): Resource<List<SlackChannelInfo>>
    suspend fun getUsers(token: SlackToken): Resource<List<UserInfo>>
}