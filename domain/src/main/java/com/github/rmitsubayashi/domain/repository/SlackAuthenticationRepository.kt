package com.github.rmitsubayashi.domain.repository

import com.github.rmitsubayashi.domain.Resource
import com.github.rmitsubayashi.domain.model.SlackTokenInfo

interface SlackAuthenticationRepository {
    suspend fun getSlackToken(): Resource<SlackTokenInfo>
    suspend fun setSlackToken(slackTokenInfo: SlackTokenInfo): Resource<Unit>
    suspend fun generateSlackToken(code: String): Resource<SlackTokenInfo>
}