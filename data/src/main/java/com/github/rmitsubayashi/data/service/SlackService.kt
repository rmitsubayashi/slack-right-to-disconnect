package com.github.rmitsubayashi.data.service

import com.github.rmitsubayashi.data.service.model.request.SlackAuthToken
import com.github.rmitsubayashi.data.service.model.request.PostRequest
import com.github.rmitsubayashi.data.service.model.request.UserGroupRequest
import com.github.rmitsubayashi.data.service.model.response.*
import com.github.rmitsubayashi.domain.model.SlackToken
import retrofit2.http.*

interface SlackService {
    @Headers(
        "Content-type: application/json"
    )
    @POST("chat.postMessage")
    suspend fun postMessage(
        @Body postRequest: PostRequest,
        @Header("Authorization") auth: SlackAuthToken
    ): PostResponse

    @POST("auth.test")
    @FormUrlEncoded
    suspend fun validateToken(
        @Field("token") token: SlackToken
    ): ValidateTokenResponse

    @POST("channels.list")
    @FormUrlEncoded
    suspend fun getChannels(
        @Field("token") token: SlackToken,
        @Field("exclude_archived") excludeArchived: Boolean = true,
        @Field("exclude_members") excludeMembers: Boolean = true
    ): ChannelResponse

    @POST("users.list")
    @FormUrlEncoded
    suspend fun getUsers(
        @Field("token") token: SlackToken
    ): UsersResponse

    @Headers(
        "Content-type: application/json"
    )
    @POST("conversations.open")
    suspend fun createUserGroup(
        @Body userGroupRequest: UserGroupRequest,
        @Header("Authorization") auth: SlackAuthToken
    ): UserGroupResponse
}