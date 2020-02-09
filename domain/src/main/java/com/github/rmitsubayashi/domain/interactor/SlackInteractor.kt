package com.github.rmitsubayashi.domain.interactor

import com.github.rmitsubayashi.domain.Resource
import com.github.rmitsubayashi.domain.error.NetworkError
import com.github.rmitsubayashi.domain.model.Message
import com.github.rmitsubayashi.domain.repository.SlackRepository

class SlackInteractor (private val slackRepository: SlackRepository) {
    suspend fun postToSlack(message: Message, id: String): Resource<Unit> {
        val slackTokenResource = slackRepository.getSlackToken()
        slackTokenResource.error?.let {
            return Resource.error(it)
        }
        val slackToken = slackTokenResource.data ?: return Resource.error(NetworkError.RESOURCE_NOT_AVAILABLE)

        return slackRepository.post(message, id, slackToken.token)
    }
}