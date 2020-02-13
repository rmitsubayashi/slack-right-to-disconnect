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

class HomeInteractor(
    private val slackInteractor: SlackInteractor,
    val messageInputInteractor: MessageInputInteractor,
    private val slackRepository: SlackRepository
    ) {
    private var recipientID: String = ""
    private var threadID: String? = null
    suspend fun post(): Resource<Unit> {
        val formattedMessage = messageInputInteractor.formatMessageForSlack()
        return slackInteractor.postToSlack(formattedMessage, recipientID, threadID)
    }

    suspend fun getUsers(): Resource<List<UserInfo>> {
        return slackRepository.getUsers()
    }

    suspend fun getChannels(): Resource<List<SlackChannelInfo>> {
        return slackRepository.getSlackChannels()
    }

    fun setRecipientID(id: String) {
        this.recipientID = id
    }

    fun setThreadID(id: String?) {
        this.threadID = id
    }

    suspend fun getRecentThreads(): Resource<List<ThreadInfo>> {
        val recentThreadsResource = slackRepository.getRecentThreads()
        recentThreadsResource.data?.let {
            val today = Date()
            val removeOldThreads = it.filter {
                ti ->
                val diff = today.time - ti.date.time
                val diffInDays = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS)
                diffInDays < 7
            }
            if (it.size > removeOldThreads.size) {
                slackRepository.updateRecentThreads(removeOldThreads)
            }
            return Resource.success(removeOldThreads)
        } ?: return recentThreadsResource
    }
}