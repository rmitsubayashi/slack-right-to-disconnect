package com.github.rmitsubayashi.domain.interactor

import com.github.rmitsubayashi.domain.Resource
import com.github.rmitsubayashi.domain.error.ValidationError
import com.github.rmitsubayashi.domain.model.RecipientType
import com.github.rmitsubayashi.domain.model.SlackChannelInfo
import com.github.rmitsubayashi.domain.model.ThreadInfo
import com.github.rmitsubayashi.domain.model.UserInfo
import com.github.rmitsubayashi.domain.repository.SlackRepository
import java.util.*
import java.util.concurrent.TimeUnit

class RecipientInteractor(
    private val slackRepository: SlackRepository
) {
    private val selectedUsers: MutableSet<UserInfo> = mutableSetOf()

    suspend fun getUsers(): Resource<List<UserInfo>> {
        return slackRepository.getUsers()
    }

    fun selectUser(user: UserInfo) {
        selectedUsers.add(user)
    }

    fun deselectUser(user: UserInfo) {
        selectedUsers.remove(user)
    }

    fun shouldSelectButtonBeEnabled(): Boolean = selectedUsers.size > 0

    suspend fun getUserGroup(): Resource<UserInfo> {
        if (selectedUsers.size == 0) {
            return Resource.error(ValidationError.EMPTY)
        }
        if (selectedUsers.size == 1) {
            return Resource.success(selectedUsers.elementAt(0))
        }
        return slackRepository.createUserGroup(selectedUsers)
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

    fun shouldShowSelectButton(recipientType: RecipientType): Boolean = recipientType == RecipientType.USER
}