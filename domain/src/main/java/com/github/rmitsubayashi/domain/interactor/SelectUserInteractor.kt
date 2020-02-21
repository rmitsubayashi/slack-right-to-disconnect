package com.github.rmitsubayashi.domain.interactor

import com.github.rmitsubayashi.domain.Resource
import com.github.rmitsubayashi.domain.error.ValidationError
import com.github.rmitsubayashi.domain.model.Recipient
import com.github.rmitsubayashi.domain.repository.SlackTeamRepository

class SelectUserInteractor(
    private val slackTeamRepository: SlackTeamRepository
) {
    private val selectedUsers: MutableSet<Recipient> = mutableSetOf()

    suspend fun getUsers(): Resource<List<Recipient>> {
        return slackTeamRepository.getUsers()
    }

    fun selectUser(user: Recipient) {
        selectedUsers.add(user)
    }

    fun deselectUser(user: Recipient) {
        selectedUsers.remove(user)
    }

    suspend fun getUserGroup(): Resource<Recipient> {
        if (selectedUsers.size == 0) {
            return Resource.error(ValidationError.EMPTY)
        }
        if (selectedUsers.size == 1) {
            return Resource.success(selectedUsers.elementAt(0))
        }
        return slackTeamRepository.createUserGroup(selectedUsers)
    }

    fun shouldShowSelectButton(): Boolean = selectedUsers.isNotEmpty()
}