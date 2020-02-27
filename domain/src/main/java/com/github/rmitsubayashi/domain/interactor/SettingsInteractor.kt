package com.github.rmitsubayashi.domain.interactor

import com.github.rmitsubayashi.domain.Resource
import com.github.rmitsubayashi.domain.error.DatabaseError
import com.github.rmitsubayashi.domain.error.GeneralError
import com.github.rmitsubayashi.domain.model.*
import com.github.rmitsubayashi.domain.repository.SlackAuthenticationRepository

class SettingsInteractor(
    private val slackAuthenticationRepository: SlackAuthenticationRepository
) {
    suspend fun saveSlackToken(token: SlackToken): Resource<SlackTokenInfo> {
        val currentToken = slackAuthenticationRepository.getSlackToken()
        if (currentToken.data?.token?.value == token.value) {
            return Resource.error(DatabaseError.ALREADY_EXISTS)
        }

        val validationResource = slackAuthenticationRepository.validateSlackToken(token)
        return when (validationResource.error) {
            null -> {
                validationResource.data?.let {
                    slackAuthenticationRepository.setSlackToken(it)
                    validationResource
                } ?: Resource.error(GeneralError.UNEXPECTED)
            }
            else -> Resource.error(validationResource.error)
        }
    }

    suspend fun getCurrentSlackTokenInfo(): Resource<SlackTokenInfo> {
        return slackAuthenticationRepository.getSlackToken()
    }
}