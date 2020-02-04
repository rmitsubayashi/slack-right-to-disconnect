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
    private var slackChannels: List<SlackChannelInfo>? = null
    suspend fun findSlackChannelByID(id: SlackChannelID): SlackChannelInfo? {
        if (slackChannels == null) {
            getChannels()
        }
        slackChannels?.map { if (it.id.value == id.value) {return it} }
        return null
    }

    suspend fun saveSlackChannel(channelID: SlackChannelID?): Resource<Unit> {
        if (channelID == null) {
            return slackRepository.clearSlackChannel()
        }

        val currentSlackChannelID = slackRepository.getSlackChannelID()
        if (currentSlackChannelID.data?.value == channelID.value) {
            return Resource.error(DatabaseError.ALREADY_EXISTS)
        }

        val slackTokenResource = slackRepository.getSlackToken()
        val slackToken =
            slackTokenResource.data ?: return Resource.error(NetworkError.RESOURCE_NOT_AVAILABLE)
        val validationResource = slackRepository.slackChannelExists(slackToken.token, channelID)
        validationResource.error?.let { return Resource.error(it) }
        return when (validationResource.data) {
            null -> Resource.error(GeneralError.UNEXPECTED)
            false -> Resource.error(
                ValidationError.INVALID_SLACK_CHANNEL_ID
            )
            true -> {
                val idResult = slackRepository.setSlackChannelID(channelID)
                if (idResult.error != null) {
                    Resource.error<Unit>(GeneralError.UNEXPECTED)
                }
                val channel = this.findSlackChannelByID(channelID)
                if (channel == null) {
                    Resource.error(GeneralError.UNEXPECTED)
                } else {
                    val nameResult = slackRepository.setSlackChannelName(channel.name)
                    if (nameResult.error != null) {
                        Resource.error(GeneralError.UNEXPECTED)
                    } else {
                        Resource.success(null)
                    }
                }


            }
        }
    }

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

    suspend fun getCurrentSlackChannel(): Resource<SlackChannelInfo> {
        val idResource = slackRepository.getSlackChannelID()
        val nameResource = slackRepository.getSlackChannelName()
        if (idResource.data == null) {
            return Resource.error(NetworkError.RESOURCE_NOT_AVAILABLE)
        }
        if (nameResource.data == null) {
            return Resource.error(NetworkError.RESOURCE_NOT_AVAILABLE)
        }
        return Resource.success(SlackChannelInfo(idResource.data, nameResource.data))
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

    suspend fun getChannels(): Resource<List<SlackChannelInfo>> {
        val tokenResource = slackRepository.getSlackToken()
        if (tokenResource.data == null || tokenResource.data.token.value.isEmpty()) {
            return Resource.error(ValidationError.INVALID_SLACK_TOKEN)
        }
        val channelsResource = slackRepository.getSlackChannels(tokenResource.data.token)
        this.slackChannels = channelsResource.data
        return channelsResource
    }
}