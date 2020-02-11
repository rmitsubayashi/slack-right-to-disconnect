package com.github.rmitsubayashi.data.repository

import android.content.SharedPreferences
import androidx.core.content.edit
import com.github.rmitsubayashi.data.model.SlackAuthToken
import com.github.rmitsubayashi.data.model.request.PostRequest
import com.github.rmitsubayashi.data.model.response.UsersResponseUser
import com.github.rmitsubayashi.data.service.SlackService
import com.github.rmitsubayashi.data.util.ConnectionManager
import com.github.rmitsubayashi.domain.Resource
import com.github.rmitsubayashi.domain.error.NetworkError
import com.github.rmitsubayashi.domain.error.ValidationError
import com.github.rmitsubayashi.domain.model.*
import com.github.rmitsubayashi.domain.repository.SlackRepository
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.*

internal class SlackDataRepository(
    private val secureSharedPreferences: SharedPreferences,
    private val sharedPreferences: SharedPreferences,
    private val slackService: SlackService,
    private val connectionManager: ConnectionManager
) : SlackRepository {
    override suspend fun getSlackToken(): Resource<SlackTokenInfo> {
        val token = secureSharedPreferences.getString(SecureSharedPrefKeys.SLACK_TOKEN, "")
            ?: return Resource.error(NetworkError.RESOURCE_NOT_AVAILABLE)
        val user = sharedPreferences.getString(SharedPrefsKeys.SLACK_TOKEN_USER, "")
            ?: return Resource.error(NetworkError.RESOURCE_NOT_AVAILABLE)
        val team = sharedPreferences.getString(SharedPrefsKeys.SLACK_TOKEN_TEAM, "")
            ?: return Resource.error(NetworkError.RESOURCE_NOT_AVAILABLE)
        return Resource.success(SlackTokenInfo(SlackToken(token), user = user, team = team))
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

    override suspend fun validateSlackToken(slackToken: SlackToken): Resource<SlackTokenInfo> {
        if (!connectionManager.isConnected()) {
            return Resource.error(NetworkError.NOT_CONNECTED)
        }
        val response = slackService.validateToken(slackToken)
        return if (response.valid) {
            val info = SlackTokenInfo(slackToken, user = response.user, team = response.team)
            Resource.success(info)
        } else {
            Resource.error(ValidationError.INVALID_SLACK_TOKEN)
        }
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
            response.channels.map { SlackChannelInfo(it.channelID, it.name) }
        )
    }

    override suspend fun post(
        message: Message,
        id: String,
        token: SlackToken,
        threadID: String?
    ): Resource<String> {
        if (!connectionManager.isConnected()) {
            return Resource.error(NetworkError.NOT_CONNECTED)
        }
        val response =
            slackService.postMessage(PostRequest(message, id, threadID = threadID), SlackAuthToken(token))
        return if (response.sent) {
            Resource.success(response.threadIdentifier)
        } else {
            Resource.error(NetworkError.NETWORK_ERROR)
        }
    }

    override suspend fun getUsers(token: SlackToken): Resource<List<UserInfo>> {
        if (!connectionManager.isConnected()) {
            return Resource.error(NetworkError.NOT_CONNECTED)
        }
        val response = slackService.getUsers(token)
        if (!response.success) {
            return Resource.error(NetworkError.NETWORK_ERROR)
        }
        val filteredUsers = removeBotsAndInactiveUsers(response.users)
        return Resource.success(
            filteredUsers.map {
                UserInfo(it.userID, name = it.username)
            }
        )
    }

    private fun removeBotsAndInactiveUsers(users: List<UsersResponseUser>): List<UsersResponseUser> {
        return users.filter {
            !it.deleted &&
                    !it.isBot &&
                    //何故かSlackbotはbotじゃない。。
                    it.userID != "USLACKBOT"
        }
    }

    override fun saveThreadInfo(id: String, message: Message, threadID: String) {
        val list = getThreadInfo()
        val threadInfo = ThreadInfo(id, message.value, Date(), threadID)
        val newList = list.plus(threadInfo)
        val json = Gson().toJson(newList)
        sharedPreferences.edit {
            putString(SharedPrefsKeys.RECENT_THREADS, json)
        }
    }

    private fun getThreadInfo(): List<ThreadInfo> {
        val json = sharedPreferences.getString(SharedPrefsKeys.RECENT_THREADS, null) ?: return emptyList()
        val type = object: TypeToken<List<ThreadInfo>>(){}.type
        return Gson().fromJson(json, type)
    }

    override suspend fun getRecentThreads(): Resource<List<ThreadInfo>> {
        val list = getThreadInfo()
        return Resource.success(list)
    }
}