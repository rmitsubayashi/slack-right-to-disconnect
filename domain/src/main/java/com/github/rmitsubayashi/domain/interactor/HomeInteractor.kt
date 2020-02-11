package com.github.rmitsubayashi.domain.interactor

import com.github.rmitsubayashi.domain.Resource
import com.github.rmitsubayashi.domain.error.ValidationError
import com.github.rmitsubayashi.domain.model.Message
import com.github.rmitsubayashi.domain.model.SlackChannelInfo
import com.github.rmitsubayashi.domain.model.ThreadInfo
import com.github.rmitsubayashi.domain.model.UserInfo
import com.github.rmitsubayashi.domain.repository.SlackRepository

class HomeInteractor(
    private val slackInteractor: SlackInteractor,
    private val slackRepository: SlackRepository
    ) {
    private var message: Message = Message("")
    private var recipientID: String = ""
    private var threadID: String? = null
    suspend fun post(): Resource<Unit> {
        return slackInteractor.postToSlack(message, recipientID, threadID)
    }

    fun updateMessage(message: Message): Resource<Unit> {
        this.message = message
        return Resource.success(null)
    }

    suspend fun getUsers(): Resource<List<UserInfo>> {
        val tokenResource = slackRepository.getSlackToken()
        if (tokenResource.data == null || tokenResource.data.token.value.isEmpty()) {
            return Resource.error(ValidationError.INVALID_SLACK_TOKEN)
        }
        return slackRepository.getUsers(tokenResource.data.token)
    }

    suspend fun getChannels(): Resource<List<SlackChannelInfo>> {
        val tokenResource = slackRepository.getSlackToken()
        if (tokenResource.data == null || tokenResource.data.token.value.isEmpty()) {
            return Resource.error(ValidationError.INVALID_SLACK_TOKEN)
        }
        return slackRepository.getSlackChannels(tokenResource.data.token)
    }

    fun setRecipientID(id: String) {
        this.recipientID = id
    }

    fun setThreadID(id: String?) {
        this.threadID = id
    }

    suspend fun getRecentThreads(): Resource<List<ThreadInfo>> {
        return slackRepository.getRecentThreads()
    }
}