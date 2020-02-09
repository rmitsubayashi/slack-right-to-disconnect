package com.github.rmitsubayashi.domain.interactor

import com.github.rmitsubayashi.domain.Resource
import com.github.rmitsubayashi.domain.error.DatabaseError
import com.github.rmitsubayashi.domain.error.GeneralError
import com.github.rmitsubayashi.domain.error.NetworkError
import com.github.rmitsubayashi.domain.error.ValidationError
import com.github.rmitsubayashi.domain.model.*
import com.github.rmitsubayashi.domain.repository.SlackRepository

class SettingsInteractor(
    private val slackRepository: SlackRepository
) {
    suspend fun saveSlackToken(token: SlackToken): Resource<SlackTokenInfo> {
        val currentToken = slackRepository.getSlackToken()
        if (currentToken.data?.token?.value == token.value) {
            return Resource.error(DatabaseError.ALREADY_EXISTS)
        }

        val validationResource = slackRepository.validateSlackToken(token)
        validationResource.error?.let { return Resource.error(it) }
        return when (validationResource.error) {
            null -> {
                validationResource.data?.let {
                    slackRepository.setSlackToken(it)
                    validationResource
                } ?: Resource.error(GeneralError.UNEXPECTED)
            }
            else -> Resource.error(
                ValidationError.INVALID_SLACK_TOKEN
            )
        }
    }

    suspend fun getCurrentSlackTokenInfo(): Resource<SlackTokenInfo> {
        val tokenResource = slackRepository.getSlackToken()
        return when (tokenResource.error) {
            null ->
                if (tokenResource.data == null || tokenResource.data.token.value.isEmpty()) {
                    Resource.error(NetworkError.RESOURCE_NOT_AVAILABLE)
                } else {
                    Resource.success(tokenResource.data)
                }
            else -> Resource.error(tokenResource.error)
        }
    }
}