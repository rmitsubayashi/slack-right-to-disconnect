package com.github.rmitsubayashi.data.repository

import android.content.SharedPreferences
import androidx.core.content.edit
import com.github.rmitsubayashi.data.model.SlackAuthToken
import com.github.rmitsubayashi.data.model.request.PostRequest
import com.github.rmitsubayashi.data.service.SlackService
import com.github.rmitsubayashi.data.util.ConnectionManager
import com.github.rmitsubayashi.domain.Resource
import com.github.rmitsubayashi.domain.error.NetworkError
import com.github.rmitsubayashi.domain.error.ValidationError
import com.github.rmitsubayashi.domain.model.*
import com.github.rmitsubayashi.domain.repository.SlackRepository

internal class SlackDataRepository(
    private val secureSharedPreferences: SharedPreferences,
    private val sharedPreferences: SharedPreferences,
    private val slackService: SlackService,
    private val connectionManager: ConnectionManager
) : SlackRepository {
    override suspend fun getSlackChannelID(): Resource<SlackChannelID> {
        val channelID = sharedPreferences.getString(SharedPrefsKeys.SLACK_CHANNEL_ID, "")
            ?: return Resource.error(NetworkError.RESOURCE_NOT_AVAILABLE)
        return Resource.success(SlackChannelID(channelID))
    }

    override suspend fun setSlackChannelID(channelID: SlackChannelID): Resource<Unit> {
        sharedPreferences.edit { putString(SharedPrefsKeys.SLACK_CHANNEL_ID, channelID.value) }
        return Resource.success(null)
    }

    override suspend fun clearSlackChannel(): Resource<Unit> {
        sharedPreferences.edit { putString(SharedPrefsKeys.SLACK_CHANNEL_ID, "") }
        sharedPreferences.edit { putString(SharedPrefsKeys.SLACK_CHANNEL_NAME, "") }
        return Resource.success(null)
    }

    override suspend fun getSlackToken(): Resource<SlackTokenInfo> {
        val token = secureSharedPreferences.getString(SecureSharedPrefKeys.SLACK_TOKEN, "")
            ?: return Resource.error(NetworkError.RESOURCE_NOT_AVAILABLE)
        val user = sharedPreferences.getString(SharedPrefsKeys.SLACK_TOKEN_USER, "")
            ?: return Resource.error(NetworkError.RESOURCE_NOT_AVAILABLE)
        val team = sharedPreferences.getString(SharedPrefsKeys.SLACK_TOKEN_TEAM, "")
            ?: return Resource.error(NetworkError.RESOURCE_NOT_AVAILABLE)
        return Resource.success(SlackTokenInfo(SlackToken(token), user=user, team=team))
    }

    override suspend fun setSlackToken(slackTokenInfo: SlackTokenInfo): Resource<Unit> {
        secureSharedPreferences.edit {
            putString(
                SecureSharedPrefKeys.SLACK_TOKEN,
                slackTokenInfo.token.value
            )
        }
        sharedPreferences.edit {
            putString(SharedPrefsKeys.SLACK_TOKEN_USER, slackTokenInfo.user)
            putString(SharedPrefsKeys.SLACK_TOKEN_TEAM, slackTokenInfo.team)
        }

        return Resource.success(null)
    }

    override suspend fun getSlackChannelName(): Resource<String> {
        val name = sharedPreferences.getString(SharedPrefsKeys.SLACK_CHANNEL_NAME, "")
            ?: return Resource.error(NetworkError.RESOURCE_NOT_AVAILABLE)
        return Resource.success(name)
    }

    override suspend fun setSlackChannelName(name: String): Resource<Unit> {
        sharedPreferences.edit {
            putString(
                SharedPrefsKeys.SLACK_CHANNEL_NAME,
                name
            )
        }
        return Resource.success(null)
    }

    override suspend fun validateSlackToken(slackToken: SlackToken): Resource<SlackTokenInfo> {
        if (!connectionManager.isConnected()) {
            return Resource.error(NetworkError.NOT_CONNECTED)
        }
        val response = slackService.validateToken(slackToken)
        return if (response.valid) {
            val info = SlackTokenInfo(slackToken, user=response.user, team=response.team)
            Resource.success(info)
        } else {
            Resource.error(ValidationError.INVALID_SLACK_TOKEN)
        }
    }

    override suspend fun slackChannelExists(
        slackToken: SlackToken,
        slackChannelID: SlackChannelID
    ): Resource<Boolean> {
        if (!connectionManager.isConnected()) {
            return Resource.error(NetworkError.NOT_CONNECTED)
        }
        val response = slackService.validateChannel(slackToken, slackChannelID)
        return Resource.success(response.exists)
    }

    override suspend fun getSlackChannels(token: SlackToken): Resource<List<SlackChannelInfo>> {
        if (!connectionManager.isConnected()) {
            return Resource.error(NetworkError.NOT_CONNECTED)
        }
        val response = slackService.getChannels(token)
        if (!response.success) {
            return Resource.error(NetworkError.NETWORK_ERROR)
        }
        return Resource.success(
            response.channels.map { SlackChannelInfo(SlackChannelID(it.channelID), it.name) }
        )
    }

    override suspend fun post(
        message: Message,
        channelID: SlackChannelID,
        token: SlackToken
    ): Resource<Unit> {
        if (!connectionManager.isConnected()) {
            return Resource.error(NetworkError.NOT_CONNECTED)
        }
        val response =
            slackService.postMessage(PostRequest(message, channelID), SlackAuthToken(token))
        return if (response.sent) {
            Resource.success(null)
        } else {
            Resource.error(NetworkError.NETWORK_ERROR)
        }
    }
}