package com.github.rmitsubayashi.domain.interactor

import com.github.rmitsubayashi.domain.Resource
import com.github.rmitsubayashi.domain.model.SlackChannelInfo
import com.github.rmitsubayashi.domain.model.ThreadInfo
import com.github.rmitsubayashi.domain.model.UserInfo
import com.github.rmitsubayashi.domain.repository.SlackRepository
import java.util.*
import java.util.concurrent.TimeUnit

class RecipientInteractor(
    private val slackRepository: SlackRepository
) {
    suspend fun getUsers(): Resource<List<UserInfo>> {
        return slackRepository.getUsers()
    }

    suspend fun getChannels(): Resource<List<SlackChannelInfo>> {
        return slackRepository.getSlackChannels()
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