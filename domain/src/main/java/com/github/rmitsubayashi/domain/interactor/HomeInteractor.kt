package com.github.rmitsubayashi.domain.interactor

import com.github.rmitsubayashi.domain.Resource
import com.github.rmitsubayashi.domain.error.ValidationError
import com.github.rmitsubayashi.domain.model.LateTime
import com.github.rmitsubayashi.domain.model.MessageTemplate
import com.github.rmitsubayashi.domain.repository.MessageRepository

class HomeInteractor(
    private val slackInteractor: SlackInteractor,
    private val messageRepository: MessageRepository
    ) {
    private var messageTemplate: MessageTemplate = MessageTemplate("")
    private var lateTime: LateTime? = null

    suspend fun post(): Resource<Unit> {
        if (messageTemplate.hasLateTimePlaceHolder() && lateTime == null) {
            return Resource.error(ValidationError.EMPTY_LATE_TIME)
        }

        val message = messageTemplate.createMessage(lateTime)
        return slackInteractor.postToSlack(message)
    }

    fun updateMessageTemplate(messageTemplate: MessageTemplate): Resource<Unit> {
        this.messageTemplate = messageTemplate
        return Resource.success(null)
    }

    fun updateLateTime(lateTime: LateTime): Resource<Unit> {
        this.lateTime = if (lateTime.notSet()) { null } else { lateTime }
        return Resource.success(null)
    }

    private fun LateTime.notSet(): Boolean {
        return this.hours == 0 && this.minutes == 0
    }

    suspend fun loadMessageTemplates(): Resource<List<MessageTemplate>> = messageRepository.getMessageTemplates()

    fun getPreview(): String {
        return messageTemplate.createMessage(this.lateTime).value
    }
}