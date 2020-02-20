package com.github.rmitsubayashi.domain.repository

import com.github.rmitsubayashi.domain.Resource
import com.github.rmitsubayashi.domain.model.Recipient

interface SlackTeamRepository {
    suspend fun getSlackChannels(): Resource<List<Recipient>>
    suspend fun getUsers(): Resource<List<Recipient>>
    suspend fun createUserGroup(users: Collection<Recipient>): Resource<Recipient>

}