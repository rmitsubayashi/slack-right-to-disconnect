package com.github.rmitsubayashi.domain.interactor

import com.github.rmitsubayashi.domain.Resource
import com.github.rmitsubayashi.domain.error.ValidationError
import com.github.rmitsubayashi.domain.model.Recipient
import com.github.rmitsubayashi.domain.repository.SlackAuthenticationRepository
import com.github.rmitsubayashi.domain.repository.SlackTeamRepository
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

class SelectUserInteractor(
    private val slackTeamRepository: SlackTeamRepository,
    private val slackAuthenticationRepository: SlackAuthenticationRepository
) {
    private val lock = ReentrantLock()
    private val selectedUsers: MutableSet<Recipient> = mutableSetOf()

    suspend fun getUsers(): Resource<List<Recipient>> {
        val resource = slackTeamRepository.getUsers()
        resource.data?.let {
            val sortedList = it.sortedBy { recipient -> recipient.displayName }
            return Resource.success(sortedList)
        } ?: return resource
    }

    fun selectUser(user: Recipient) {
        lock.withLock {
            selectedUsers.add(user)
        }
    }

    fun deselectUser(user: Recipient) {
        lock.withLock {
            selectedUsers.remove(user)
        }
    }

    suspend fun getUserGroup(): Resource<Recipient> {
        val size = lock.withLock { selectedUsers.size }
        if (size == 0) {
            return Resource.error(ValidationError.EMPTY)
        }
        if (size == 1) {
            return Resource.success(selectedUsers.elementAt(0))
        }
        if (size == 2) {
            val tokenResource = slackAuthenticationRepository.getSlackToken()
            val userID = tokenResource.data?.userID ?: return slackTeamRepository.createUserGroup(
                selectedUsers
            )
            val thisUser = selectedUsers.find { it.slackID == userID }
            if (thisUser != null) {
                //trying to create a user group with yourself and another person
                // returns not enough people error
                val copy = mutableListOf<Recipient>().apply {addAll(selectedUsers)}
                copy.remove(thisUser)
                return Resource.success(copy[0])
            }
        }
        return slackTeamRepository.createUserGroup(selectedUsers)
    }

    fun shouldShowSelectButton(): Boolean = selectedUsers.isNotEmpty()
}