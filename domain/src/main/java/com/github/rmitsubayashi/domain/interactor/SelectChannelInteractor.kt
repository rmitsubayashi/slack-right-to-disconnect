package com.github.rmitsubayashi.domain.interactor

import com.github.rmitsubayashi.domain.Resource
import com.github.rmitsubayashi.domain.model.Recipient
import com.github.rmitsubayashi.domain.repository.SlackTeamRepository

class SelectChannelInteractor(
    private val slackTeamRepository: SlackTeamRepository
) {
    suspend fun getChannels(): Resource<List<Recipient>> {
        val resource = slackTeamRepository.getSlackChannels()
        resource.data?.let {
            val orderedList = it.sortedBy { recipient -> recipient.displayName }
            return Resource.success(orderedList)
        } ?: return resource
    }
}