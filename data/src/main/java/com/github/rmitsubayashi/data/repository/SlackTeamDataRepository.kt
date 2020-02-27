package com.github.rmitsubayashi.data.repository

import com.github.rmitsubayashi.data.service.model.request.SlackAuthToken
import com.github.rmitsubayashi.data.service.model.request.UserGroupRequest
import com.github.rmitsubayashi.data.service.model.response.UsersResponseUser
import com.github.rmitsubayashi.data.service.SlackService
import com.github.rmitsubayashi.data.util.ConnectionManager
import com.github.rmitsubayashi.domain.Resource
import com.github.rmitsubayashi.domain.error.DatabaseError
import com.github.rmitsubayashi.domain.error.NetworkError
import com.github.rmitsubayashi.domain.error.SlackError
import com.github.rmitsubayashi.domain.model.Recipient
import com.github.rmitsubayashi.domain.model.RecipientType
import com.github.rmitsubayashi.domain.repository.SlackTeamRepository
import com.github.rmitsubayashi.domain.repository.SlackAuthenticationRepository

class SlackTeamDataRepository(
    private val slackService: SlackService,
    private val connectionManager: ConnectionManager,
    private val slackAuthenticationRepository: SlackAuthenticationRepository
): SlackTeamRepository {
    override suspend fun getSlackChannels(): Resource<List<Recipient>> {
        if (!connectionManager.isConnected()) {
            return Resource.error(NetworkError.NOT_CONNECTED)
        }
        val tokenResource = slackAuthenticationRepository.getSlackToken()
        val tokenInfo = tokenResource.data ?: return Resource.error(DatabaseError.DOES_NOT_EXIST)
        val response = slackService.getChannels(tokenInfo.token)
        if (!response.success) {
            if (response.error == "limit_required") {
                return Resource.error(SlackError.TOO_MANY_CHANNELS)
            }
            return Resource.error()
        }
        return Resource.success(
            response.channels.map { Recipient(it.channelID, it.name, RecipientType.CHANNEL) }
        )
    }

    private var userCache: List<Recipient>? = null
    override suspend fun getUsers(): Resource<List<Recipient>> {
        if (userCache != null) {
            return Resource.success(userCache)
        }
        if (!connectionManager.isConnected()) {
            return Resource.error(NetworkError.NOT_CONNECTED)
        }
        val tokenResource = slackAuthenticationRepository.getSlackToken()
        val tokenInfo = tokenResource.data ?: return Resource.error(DatabaseError.DOES_NOT_EXIST)
        val response = slackService.getUsers(tokenInfo.token)
        if (!response.success) {
            if (response.error == "limit_required") {
                return Resource.error(SlackError.TOO_MANY_USERS)
            }
            return Resource.error()
        }
        val filteredUsers = removeBotsAndInactiveUsers(response.users)
        userCache = filteredUsers.map {
            Recipient(it.userID, it.username, RecipientType.USER)
        }
        return Resource.success(userCache)
    }

    private fun removeBotsAndInactiveUsers(users: List<UsersResponseUser>): List<UsersResponseUser> {
        return users.filter {
            !it.deleted &&
                    !it.isBot &&
                    //何故かSlackbotはbotじゃない。。
                    it.userID != "USLACKBOT"
        }
    }

    override suspend fun createUserGroup(users: Collection<Recipient>): Resource<Recipient> {
        if (!connectionManager.isConnected()) {
            return Resource.error(NetworkError.NOT_CONNECTED)
        }
        val tokenResource = slackAuthenticationRepository.getSlackToken()
        val tokenInfo = tokenResource.data ?: return Resource.error(DatabaseError.DOES_NOT_EXIST)
        val usersString = users.map { it.slackID }.joinToString(",")
        val authToken = SlackAuthToken(tokenInfo.token)
        val response = slackService.createUserGroup(UserGroupRequest(usersString), authToken)
        if (!response.success) {
            if (response.error == "too_many_users") {
                return Resource.error(SlackError.TOO_MANY_USERS)
            }
            return Resource.error()
        }
        val userName = users.map { it.displayName }.joinToString(", ")
        return Resource.success(
            Recipient(response.channel.id, userName, RecipientType.USER)
        )
    }
}