package com.github.rmitsubayashi.domain.interactor

import com.github.rmitsubayashi.domain.Resource
import com.github.rmitsubayashi.domain.error.DatabaseError
import com.github.rmitsubayashi.domain.error.GeneralError
import com.github.rmitsubayashi.domain.model.*
import com.github.rmitsubayashi.domain.repository.SlackAuthenticationRepository

class SettingsInteractor(
    private val slackAuthenticationRepository: SlackAuthenticationRepository
) {
    suspend fun getCurrentSlackTokenInfo(): Resource<SlackTokenInfo> {
        return slackAuthenticationRepository.getSlackToken()
    }
}