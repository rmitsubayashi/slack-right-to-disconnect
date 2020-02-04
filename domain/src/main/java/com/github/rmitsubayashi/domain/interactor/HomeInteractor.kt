package com.github.rmitsubayashi.domain.interactor

import com.github.rmitsubayashi.domain.Resource
import com.github.rmitsubayashi.domain.model.Message

class HomeInteractor(
    private val slackInteractor: SlackInteractor
    ) {
    var message: Message = Message("")
    suspend fun post(): Resource<Unit> {
        return slackInteractor.postToSlack(message)
    }

    fun updateMessage(message: Message): Resource<Unit> {
        this.message = message
        return Resource.success(null)
    }
}