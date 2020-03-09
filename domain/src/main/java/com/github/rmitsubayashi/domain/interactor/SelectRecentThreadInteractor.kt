package com.github.rmitsubayashi.domain.interactor

import com.github.rmitsubayashi.domain.Resource
import com.github.rmitsubayashi.domain.model.Message
import com.github.rmitsubayashi.domain.repository.SlackMessageRepository
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.math.abs

class SelectRecentThreadInteractor(
    private val slackMessageRepository: SlackMessageRepository
) {
    suspend fun getRecentThreads(): Resource<List<Message>> {
        val recentThreadsResource = slackMessageRepository.getRecentThreads()
        recentThreadsResource.data?.let {
            val today = Date()
            val removeOldThreads = it.filter {
                    ti ->
                val diff = today.time - ti.date.time
                val diffInDays = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS)
                diffInDays < 7
            }
            if (it.size > removeOldThreads.size) {
                slackMessageRepository.updateRecentThreads(removeOldThreads)
            }
            val sorted = removeOldThreads.sortedByDescending { newThreads -> newThreads.date }
            return Resource.success(sorted)
        } ?: return recentThreadsResource
    }

    fun getDaysAgo(message: Message): Int {
        val millisecondsBetw = abs(message.date.time - Date().time)
        val diff = TimeUnit.DAYS.convert(millisecondsBetw, TimeUnit.MILLISECONDS)
        return diff.toInt()
    }
}