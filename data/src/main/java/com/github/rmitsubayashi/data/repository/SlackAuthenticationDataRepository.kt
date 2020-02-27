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
import com.github.rmitsubayashi.domain.model.SlackToken
import com.github.rmitsubayashi.domain.model.SlackTokenInfo
import com.github.rmitsubayashi.domain.repository.SlackAuthenticationRepository

class SlackAuthenticationDataRepository(
    private val secureSharedPreferences: SharedPreferences,
    private val sharedPreferences: SharedPreferences,
    private val slackService: SlackService,
    private val connectionManager: ConnectionManager
): SlackAuthenticationRepository {
    override suspend fun getSlackToken(): Resource<SlackTokenInfo> {
        val token = secureSharedPreferences.getString(SecureSharedPrefKeys.SLACK_TOKEN, null)
            ?: return Resource.error(DatabaseError.DOES_NOT_EXIST)
        val user = sharedPreferences.getString(SharedPrefsKeys.SLACK_TOKEN_USER, null)
            ?: return Resource.error(DatabaseError.DOES_NOT_EXIST)
        val team = sharedPreferences.getString(SharedPrefsKeys.SLACK_TOKEN_TEAM, null)
            ?: return Resource.error(DatabaseError.DOES_NOT_EXIST)
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

        return Resource.success()
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
            Resource.error(SlackError.INVALID_SLACK_TOKEN)
        }
    }

    override suspend fun generateSlackToken(code: String): Resource<String> {
        if (!connectionManager.isConnected()) {
            return Resource.error(NetworkError.NOT_CONNECTED)
        }
        val credentials = SlackClientCredentials(BuildConfig.SLACK_CLIENT_ID, BuildConfig.SLACK_CLIENT_SECRET)
        val response = slackService.generateAccessToken(credentials.generateEncodedString(), code)
        return if (response.success) {
            Resource.success(response.token)
        } else {
            Resource.error(SlackError.INVALID_SLACK_TOKEN)
        }
    }
}