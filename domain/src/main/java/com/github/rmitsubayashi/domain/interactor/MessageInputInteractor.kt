package com.github.rmitsubayashi.domain.interactor

import com.github.rmitsubayashi.domain.model.Message
import com.github.rmitsubayashi.domain.repository.SlackRepository

class MessageInputInteractor(
    private val slackRepository: SlackRepository
) {
    private var rawInput: String = ""
    private val mentions = mutableSetOf<IntRange>()
    
    internal suspend fun formatMessageForSlack(): Message {
        val sortedMentions = mentions.sortedByDescending { it.first }
        var newMessage = rawInput
        val usersResource = slackRepository.getUsers()
        val users = usersResource.data ?: return Message(rawInput)
        for (mentionRange in sortedMentions) {
            val userString = newMessage.substring(mentionRange)
            val userID = users.find { it.name == userString }?.id ?: userString
            val slackMentionString = "<@${userID}>"
            newMessage = newMessage.replaceRange(mentionRange, slackMentionString)
        }
        return Message(newMessage)
    }
    
    fun updateInput(rawInput: String) {
        this.rawInput = rawInput
    }

    fun addMention(text: String, start: Int) {
        val range = IntRange(start, start-1+text.length)
        mentions.add(range)
        val beforeStr = rawInput.substring(0, range.first)
        val afterStr = rawInput.substring(range.first+1)
        rawInput = beforeStr + text + afterStr
    }

    fun removeMention(text: String, start: Int) {
        val range = IntRange(start, start-1+text.length)
        mentions.remove(range)
        rawInput = rawInput.replaceRange(range, "")
    }

    suspend fun searchMention(keyword: String): List<String> {
        val usersResource = slackRepository.getUsers()
        val users = usersResource.data ?: return emptyList()
        val userStrings = users.map { it.name }
        return userStrings.filter { it.startsWith(keyword) }
    }
}