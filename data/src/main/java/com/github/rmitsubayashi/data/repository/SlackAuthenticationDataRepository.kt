package com.github.rmitsubayashi.data.repository

import android.content.SharedPreferences
import androidx.core.content.edit
import com.github.rmitsubayashi.data.BuildConfig
import com.github.rmitsubayashi.data.local.sharedpreferences.SecureSharedPrefKeys
import com.github.rmitsubayashi.data.local.sharedpreferences.SharedPrefsKeys
import com.github.rmitsubayashi.data.service.SlackService
import com.github.rmitsubayashi.data.service.model.request.SlackClientCredentials
import com.github.rmitsubayashi.data.util.ConnectionManager
import com.github.rmitsubayashi.domain.Resource
import com.github.rmitsubayashi.domain.error.DatabaseError
import com.github.rmitsubayashi.domain.error.NetworkError
import com.github.rmitsubayashi.domain.error.SlackError
import com.github.rmitsubayashi.domain.model.Recipient
import com.github.rmitsubayashi.domain.model.RecipientType
import com.github.rmitsubayashi.domain.model.SlackToken
import com.github.rmitsubayashi.domain.model.SlackTokenInfo
import com.github.rmitsubayashi.domain.repository.SlackAuthenticationRepository
import com.github.rmitsubayashi.domain.repository.SlackTeamRepository

class SlackAuthenticationDataRepository(
    private val secureSharedPreferences: SharedPreferences,
    private val sharedPreferences: SharedPreferences,
    private val slackService: SlackService,
    private val connectionManager: ConnectionManager
) : SlackAuthenticationRepository {
    override suspend fun getSlackToken(): Resource<SlackTokenInfo> {
        val token = secureSharedPreferences.getString(SecureSharedPrefKeys.SLACK_TOKEN, null)
            ?: return Resource.error(DatabaseError.DOES_NOT_EXIST)
        val user = sharedPreferences.getString(SharedPrefsKeys.SLACK_TOKEN_USER, null)
            ?: return Resource.error(DatabaseError.DOES_NOT_EXIST)
        val team = sharedPreferences.getString(SharedPrefsKeys.SLACK_TOKEN_TEAM, null)
            ?: return Resource.error(DatabaseError.DOES_NOT_EXIST)
        val teamDomain = sharedPreferences.getString(SharedPrefsKeys.SLACK_TOKEN_TEAM_DOMAIN, null)
            ?: return Resource.error(DatabaseError.DOES_NOT_EXIST)
        val userID = sharedPreferences.getString(SharedPrefsKeys.SLACK_TOKEN_USER_ID, null)
            ?: return Resource.error(DatabaseError.DOES_NOT_EXIST)

        return Resource.success(SlackTokenInfo(SlackToken(token), user = user, userID = userID, teamDomain = teamDomain, team = team))
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
            putString(SharedPrefsKeys.SLACK_TOKEN_USER_ID, slackTokenInfo.userID)
            putString(SharedPrefsKeys.SLACK_TOKEN_TEAM, slackTokenInfo.team)
            putString(SharedPrefsKeys.SLACK_TOKEN_TEAM_DOMAIN, slackTokenInfo.teamDomain)
        }

        return Resource.success()
    }

    override suspend fun generateSlackToken(code: String): Resource<SlackTokenInfo> {
        if (!connectionManager.isConnected()) {
            return Resource.error(NetworkError.NOT_CONNECTED)
        }
        val credentials =
            SlackClientCredentials(BuildConfig.SLACK_CLIENT_ID, BuildConfig.SLACK_CLIENT_SECRET)
        val accessTokenResponse =
            slackService.generateAccessToken(credentials.generateEncodedString(), code)
        if (!accessTokenResponse.success) {
            return Resource.error(SlackError.INVALID_SLACK_TOKEN)
        }
        //only returns user id, so get the user's info
        val userInfoResource =
            this.getUserInfo(accessTokenResponse.userID, SlackToken(accessTokenResponse.token))
        userInfoResource.error?.let {
            return Resource.error(it)
        }
        val displayName = userInfoResource.data?.displayName ?: return Resource.error()
        val teamInfoResponse = this.getTeamInfo(SlackToken(accessTokenResponse.token))
        teamInfoResponse.error?.let {
            return Resource.error(it)
        }
        val teamDomain = teamInfoResponse.data ?: return Resource.error()
        return Resource.success(
            SlackTokenInfo(
                SlackToken(accessTokenResponse.token),
                accessTokenResponse.teamName,
                teamDomain,
                displayName,
                accessTokenResponse.userID
            )
        )

    }

    private suspend fun getUserInfo(userID: String, token: SlackToken): Resource<Recipient> {
        if (!connectionManager.isConnected()) {
            return Resource.error(NetworkError.NOT_CONNECTED)
        }
        val response = slackService.getUserInfo(token, userID)
        if (!response.success) {
            return Resource.error()
        }
        return Resource.success(
            Recipient(
                userID,
                response.user.slackName,
                response.user.displayName,
                RecipientType.USER
            )
        )
    }

    private suspend fun getTeamInfo(token: SlackToken): Resource<String> {
        if (!connectionManager.isConnected()) {
            return Resource.error(NetworkError.NOT_CONNECTED)
        }
        val response = slackService.getTeamInfo(token)
        if (!response.success) {
            return Resource.error()
        }
        return Resource.success(response.team.domain)
    }
}