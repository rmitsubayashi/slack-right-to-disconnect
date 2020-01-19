package com.github.rmitsubayashi.domain.interactor

import com.github.rmitsubayashi.domain.Resource
import com.github.rmitsubayashi.domain.error.DatabaseError
import com.github.rmitsubayashi.domain.error.GeneralError
import com.github.rmitsubayashi.domain.error.NetworkError
import com.github.rmitsubayashi.domain.error.ValidationError
import com.github.rmitsubayashi.domain.model.*
import com.github.rmitsubayashi.domain.repository.MessageRepository
import com.github.rmitsubayashi.domain.repository.SlackRepository

class SettingsInteractor(
    private val slackRepository: SlackRepository,
    private val messageRepository: MessageRepository
) {
    private var messageTemplates: List<MessageTemplate>? = null
    private var slackChannels: List<SlackChannel>? = null
    suspend fun findSlackChannelByID(id: SlackChannelID): SlackChannel? {
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
        val validationResource = slackRepository.slackChannelExists(slackToken, channelID)
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

    suspend fun saveSlackToken(token: SlackToken): Resource<Unit> {
        val currentToken = slackRepository.getSlackToken()
        if (currentToken.data?.value == token.value) {
            return Resource.error(DatabaseError.ALREADY_EXISTS)
        }

        val validationResource = slackRepository.validateSlackToken(token)
        validationResource.error?.let { return Resource.error(it) }
        return when (validationResource.error) {
            null -> slackRepository.setSlackToken(token)
            else -> Resource.error(
                ValidationError.INVALID_SLACK_TOKEN
            )
        }
    }

    suspend fun loadMessageTemplates(): Resource<List<MessageTemplate>> {
        return this.messageTemplates?.let {
            Resource.success(it)
        } ?: run {
            val messageTemplatesResource = messageRepository.getMessageTemplates()
            this.messageTemplates = messageTemplatesResource.data
            return messageTemplatesResource
        }
    }

    private val messageTemplateWarningsBypassed: MutableList<ValidationError> = mutableListOf()
    suspend fun removeMessageTemplate(messageTemplate: MessageTemplate): Resource<Unit> {
        return this.messageTemplates?.let {
            val messageTemplatesCopy = it.toMutableList()
            messageTemplatesCopy.remove(messageTemplate)
            val result = messageRepository.updateMessageTemplates(messageTemplatesCopy)
            this.messageTemplates = messageTemplatesCopy
            result
        }
            ?: Resource.error(NetworkError.NETWORK_ERROR) //assumes you have to have loaded message templates to interact w/ this
    }

    suspend fun editMessageTemplate(old: MessageTemplate, new: MessageTemplate): Resource<Unit> {
        if (!new.value.contains(MessageTemplate.LATE_TIME_PLACEHOLDER) &&
            !messageTemplateWarningsBypassed.contains(ValidationError.EMPTY_LATE_TIME)
        ) {
            messageTemplateWarningsBypassed.add(ValidationError.EMPTY_LATE_TIME)
            return Resource.error(ValidationError.EMPTY_LATE_TIME)
        }
        messageTemplateWarningsBypassed.clear()

        return this.messageTemplates?.let {
            val index = it.indexOf(old)
            val messageTemplatesCopy = it.toMutableList()
            messageTemplatesCopy[index] = new
            val result = messageRepository.updateMessageTemplates(messageTemplatesCopy)
            this.messageTemplates = messageTemplatesCopy
            result
        }
            ?: Resource.error(NetworkError.NETWORK_ERROR) //assumes you have to have loaded message templates to interact w/ this
    }

    suspend fun addMessageTemplate(messageTemplate: MessageTemplate): Resource<Unit> {
        if (!messageTemplate.value.contains(MessageTemplate.LATE_TIME_PLACEHOLDER) &&
            !messageTemplateWarningsBypassed.contains(ValidationError.EMPTY_LATE_TIME)
        ) {
            messageTemplateWarningsBypassed.add(ValidationError.EMPTY_LATE_TIME)
            return Resource.error(ValidationError.EMPTY_LATE_TIME)
        }
        messageTemplateWarningsBypassed.clear()

        return this.messageTemplates?.let {
            val messageTemplatesCopy = it.toMutableList()
            messageTemplatesCopy.add(messageTemplate)
            val result = messageRepository.updateMessageTemplates(messageTemplatesCopy)
            this.messageTemplates = messageTemplatesCopy
            result
        }
            ?: Resource.error(NetworkError.NETWORK_ERROR) //assumes you have to have loaded message templates to interact w/ this
    }

    fun dismissMessageTemplateWarning() {
        messageTemplateWarningsBypassed.clear()
    }

    suspend fun getCurrentSlackChannel(): Resource<SlackChannel> {
        val idResource = slackRepository.getSlackChannelID()
        val nameResource = slackRepository.getSlackChannelName()
        if (idResource.data == null) {
            return Resource.error(NetworkError.RESOURCE_NOT_AVAILABLE)
        }
        if (nameResource.data == null) {
            return Resource.error(NetworkError.RESOURCE_NOT_AVAILABLE)
        }
        return Resource.success(SlackChannel(idResource.data, nameResource.data))
    }

    suspend fun isSlackTokenSet(): Resource<Boolean> {
        val tokenResource = slackRepository.getSlackToken()
        return when (tokenResource.error) {
            null ->
                if (tokenResource.data == null || tokenResource.data.value.isEmpty()) {
                    Resource.success(false)
                } else {
                    Resource.success(true)
                }
            else -> Resource.error(tokenResource.error)
        }
    }

    suspend fun getChannels(): Resource<List<SlackChannel>> {
        val tokenResource = slackRepository.getSlackToken()
        if (tokenResource.data == null || tokenResource.data.value.isEmpty()) {
            return Resource.error(ValidationError.INVALID_SLACK_TOKEN)
        }
        val channelsResource = slackRepository.getSlackChannels(tokenResource.data)
        this.slackChannels = channelsResource.data
        return channelsResource
    }
}