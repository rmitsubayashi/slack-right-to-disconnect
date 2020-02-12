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
    private var users: List<UserInfo>? = null
    private var recipientID: String = ""
    private var threadID: String? = null
    private val mentions = mutableSetOf<IntRange>()
    suspend fun post(): Resource<Unit> {
        val sortedMentions = mentions.sortedByDescending { it.first }
        var newMessage = message.value
        for (mentionRange in sortedMentions) {
            val userString = newMessage.substring(mentionRange)
            val userID = users?.find { it.name == userString }?.id ?: userString
            val slackMentionString = "<@${userID}>"
            newMessage = newMessage.replaceRange(mentionRange, slackMentionString)
        }
        return slackInteractor.postToSlack(Message(newMessage), recipientID, threadID)
    }

    fun updateMessage(message: Message): Resource<Unit> {
        this.message = message
        return Resource.success(null)
    }

    fun addMention(text: String, start: Int): Resource<Unit> {
        val range = IntRange(start, start-1+text.length)
        mentions.add(range)
        val beforeStr = this.message.value.substring(0, range.first)
        val afterStr = this.message.value.substring(range.first+1)
        val newMessage = beforeStr + text + afterStr
        this.message = Message(newMessage)
        return Resource.success(null)
    }

    fun removeMention(text: String, start: Int): Resource<Unit> {
        val range = IntRange(start, start-1+text.length)
        mentions.remove(range)
        val newMessage = this.message.value.replaceRange(range, "")
        this.message = Message(newMessage)
        return Resource.success(null)
    }

    suspend fun getUsers(): Resource<List<UserInfo>> {
        if (users != null) {
            return Resource.success(users)
        }
        val tokenResource = slackRepository.getSlackToken()
        if (tokenResource.data == null || tokenResource.data.token.value.isEmpty()) {
            return Resource.error(ValidationError.INVALID_SLACK_TOKEN)
        }
        val usersResource = slackRepository.getUsers(tokenResource.data.token)
        this.users = usersResource.data
        return usersResource
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