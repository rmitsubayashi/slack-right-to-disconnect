package com.github.rmitsubayashi.domain.interactor

import com.github.rmitsubayashi.domain.repository.SlackTeamRepository

class MessageInputInteractor(
    private val slackTeamRepository: SlackTeamRepository
) {
    private var rawInput: String = ""
    private val mentions = mutableSetOf<IntRange>()
    
    internal suspend fun formatMessageForSlack(): String {
        val sortedMentions = mentions.sortedByDescending { it.first }
        var newMessage = rawInput
        val usersResource = slackTeamRepository.getUsers()
        val users = usersResource.data ?: return rawInput
        for (mentionRange in sortedMentions) {
            val userString = newMessage.substring(mentionRange)
            val userID = users.find { it.displayName == userString }?.slackID ?: userString
            val slackMentionString = "<@${userID}>"
            newMessage = newMessage.replaceRange(mentionRange, slackMentionString)
        }
        return newMessage
    }

    internal fun getRawInput(): String = rawInput
    
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
        val usersResource = slackTeamRepository.getUsers()
        val users = usersResource.data ?: return emptyList()
        val userStrings = users.map { it.displayName }
        return userStrings.filter { it.startsWith(keyword) }
    }
}