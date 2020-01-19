package com.github.rmitsubayashi.domain.repository

import com.github.rmitsubayashi.domain.Resource
import com.github.rmitsubayashi.domain.model.Message
import com.github.rmitsubayashi.domain.model.SlackChannel
import com.github.rmitsubayashi.domain.model.SlackChannelID
import com.github.rmitsubayashi.domain.model.SlackToken

interface SlackRepository {
    suspend fun getSlackChannelID(): Resource<SlackChannelID>
    suspend fun setSlackChannelID(channelID: SlackChannelID): Resource<Unit>
    suspend fun clearSlackChannel(): Resource<Unit>
    suspend fun getSlackChannelName(): Resource<String>
    suspend fun setSlackChannelName(name: String): Resource<Unit>
    suspend fun getSlackToken(): Resource<SlackToken>
    suspend fun setSlackToken(slackToken: SlackToken): Resource<Unit>
    suspend fun validateSlackToken(slackToken: SlackToken): Resource<Unit>
    suspend fun slackChannelExists(slackToken: SlackToken, slackChannelID: SlackChannelID): Resource<Boolean>
    suspend fun post(message: Message, channelID: SlackChannelID, token: SlackToken): Resource<Unit>
    suspend fun getSlackChannels(token: SlackToken): Resource<List<SlackChannel>>
}