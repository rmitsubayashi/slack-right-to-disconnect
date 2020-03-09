package com.github.rmitsubayashi.data.repository

import com.github.rmitsubayashi.data.local.sqlite.dao.ThreadDao
import com.github.rmitsubayashi.data.local.sqlite.model.Thread
import com.github.rmitsubayashi.data.service.SlackService
import com.github.rmitsubayashi.data.service.model.request.PostRequest
import com.github.rmitsubayashi.data.service.model.request.SlackAuthToken
import com.github.rmitsubayashi.data.util.ConnectionManager
import com.github.rmitsubayashi.domain.Resource
import com.github.rmitsubayashi.domain.error.DatabaseError
import com.github.rmitsubayashi.domain.error.NetworkError
import com.github.rmitsubayashi.domain.error.SlackError
import com.github.rmitsubayashi.domain.error.ValidationError
import com.github.rmitsubayashi.domain.model.Message
import com.github.rmitsubayashi.domain.model.Recipient
import com.github.rmitsubayashi.domain.repository.SlackAuthenticationRepository
import com.github.rmitsubayashi.domain.repository.SlackMessageRepository

class SlackMessageDataRepository(
    private val slackAuthenticationRepository: SlackAuthenticationRepository,
    private val slackService: SlackService,
    private val threadDao: ThreadDao,
    private val connectionManager: ConnectionManager
): SlackMessageRepository {
    override suspend fun post(message: Message): Resource<String> {
        if (!connectionManager.isConnected()) {
            return Resource.error(NetworkError.NOT_CONNECTED)
        }
        val tokenResource = slackAuthenticationRepository.getSlackToken()
        val tokenInfo = tokenResource.data ?: return Resource.error(DatabaseError.DOES_NOT_EXIST)
        val authToken =
            SlackAuthToken(tokenInfo.token)
        val response =
            slackService.postMessage(PostRequest(message.message, message.recipient.slackID, threadID = message.threadID), authToken)
        return if (response.sent) {
            Resource.success(response.threadIdentifier)
        } else {
            return when (response.error) {
                "not_in_channel","is_archived","restricted_action_read_only_channel","restricted_action_thread_only_channel", "restricted_action_non_threadable_channel" ->
                    Resource.error(SlackError.RESTRICTED_CHANNEL)
                "msg_too_long","no_text" ->
                    Resource.error(ValidationError.INVALID_CONTENT)
                else -> Resource.error()
            }
        }
    }

    override fun saveRecentThread(message: Message) {
        message.threadID?.let {
            val thread = Thread(
                message.recipient.slackID,
                message.message,
                message.date,
                it,
                message.recipient.recipientType,
                message.recipient.slackName,
                message.recipient.displayName
            )
            threadDao.insert(thread)
        }
    }

    override suspend fun getRecentThreads(): Resource<List<Message>> {
        val dataThreads = threadDao.getAll()
        val domainThreads = dataThreads.map { 
            val recipient = Recipient(it.id, it.parentSlackName, it.parentDisplayName, it.parentType)
            Message(it.message, recipient, it.threadID, it.date) 
        }
        return Resource.success(domainThreads)
    }

    override fun updateRecentThreads(recentThreads: List<Message>) {
        threadDao.delete()
        val dataThreads = recentThreads.mapNotNull {
            it.threadID?.let {
                threadID ->
                Thread(
                    it.recipient.slackID,
                    it.message,
                    it.date,
                    threadID,
                    it.recipient.recipientType,
                    it.recipient.slackName,
                    it.recipient.displayName
                )
            }
        }
        threadDao.insert(*dataThreads.toTypedArray())
    }
}