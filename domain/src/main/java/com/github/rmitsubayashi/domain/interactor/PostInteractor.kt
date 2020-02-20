package com.github.rmitsubayashi.domain.interactor

import com.github.rmitsubayashi.domain.Resource
import com.github.rmitsubayashi.domain.model.*
import com.github.rmitsubayashi.domain.repository.SlackMessageRepository
import java.util.Date

class PostInteractor(
    private val slackMessageRepository: SlackMessageRepository,
    val messageInputInteractor: MessageInputInteractor
    ) {
    private var recipient: Recipient? = null
    private var threadID: String? = null
    suspend fun post(): Resource<Unit> {
        val formattedMessage = messageInputInteractor.formatMessageForSlack()
        val postResource = slackMessageRepository.post(Message(formattedMessage, recipient!!, threadID, Date()))
        postResource.data?.let {
            if (shouldSaveMessage()) {
                slackMessageRepository.saveRecentThread(Message(formattedMessage, recipient!!, it, Date()))
            }
        }
        return if (postResource.error != null) {
            Resource.error(postResource.error)
        } else {
            Resource.success()
        }
    }

    private fun shouldSaveMessage(): Boolean {
        return threadID == null
    }

    fun setRecipient(recipient: Recipient) {
        this.recipient = recipient
    }

    fun setThreadID(id: String?) {
        this.threadID = id
    }
}