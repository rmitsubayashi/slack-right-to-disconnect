package com.github.rmitsubayashi.domain.interactor

import com.github.rmitsubayashi.domain.Resource
import com.github.rmitsubayashi.domain.error.ValidationError
import com.github.rmitsubayashi.domain.model.Message
import com.github.rmitsubayashi.domain.model.SlackChannelInfo
import com.github.rmitsubayashi.domain.model.ThreadInfo
import com.github.rmitsubayashi.domain.model.UserInfo
import com.github.rmitsubayashi.domain.repository.SlackRepository
import java.util.*
import java.util.concurrent.TimeUnit

class PostInteractor(
    private val slackRepository: SlackRepository,
    val messageInputInteractor: MessageInputInteractor
    ) {
    private var recipientID: String = ""
    private var threadID: String? = null
    suspend fun post(): Resource<Unit> {
        val formattedMessage = messageInputInteractor.formatMessageForSlack()
        val postResource = slackRepository.post(formattedMessage, recipientID, threadID)
        postResource.data?.let {
            if (shouldSaveThreadInfo(threadID)) {
                slackRepository.saveThreadInfo(recipientID, messageInputInteractor.getRawMessage(), it)
            }
        }
        return if (postResource.error != null) {
            Resource.error(postResource.error)
        } else {
            Resource.success()
        }
    }

    private fun shouldSaveThreadInfo(threadID: String?): Boolean {
        return threadID.isNullOrBlank()
    }

    fun setRecipientID(id: String) {
        this.recipientID = id
    }

    fun setThreadID(id: String?) {
        this.threadID = id
    }
}