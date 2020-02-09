package com.github.rmitsubayashi.domain.repository

import com.github.rmitsubayashi.domain.Resource
import com.github.rmitsubayashi.domain.model.*

interface SlackRepository {
    suspend fun getSlackChannelID(): Resource<SlackChannelID>
    suspend fun setSlackChannelID(channelID: SlackChannelID): Resource<Unit>
    suspend fun clearSlackChannel(): Resource<Unit>
    suspend fun getSlackChannelName(): Resource<String>
    suspend fun setSlackChannelName(name: String): Resource<Unit>
    suspend fun getSlackToken(): Resource<SlackTokenInfo>
    suspend fun setSlackToken(slackTokenInfo: SlackTokenInfo): Resource<Unit>
    suspend fun validateSlackToken(slackToken: SlackToken): Resource<SlackTokenInfo>
    suspend fun slackChannelExists(slackToken: SlackToken, slackChannelID: SlackChannelID): Resource<Boolean>
    suspend fun post(message: Message, channelID: SlackChannelID, token: SlackToken): Resource<Unit>
    suspend fun getSlackChannels(token: SlackToken): Resource<List<SlackChannelInfo>>
    suspend fun getUsers(token: SlackToken): Resource<List<UserInfo>>
}