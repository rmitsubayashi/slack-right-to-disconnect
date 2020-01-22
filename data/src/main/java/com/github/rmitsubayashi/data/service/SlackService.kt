package com.github.rmitsubayashi.data.service

import com.github.rmitsubayashi.data.model.SlackAuthToken
import com.github.rmitsubayashi.data.model.request.PostRequest
import com.github.rmitsubayashi.data.model.response.*
import com.github.rmitsubayashi.domain.model.SlackChannelID
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

    @POST("channels.info")
    @FormUrlEncoded
    suspend fun validateChannel(
        @Field("token") token: SlackToken,
        @Field("channel") channelID: SlackChannelID
    ): ValidateChannelResponse

    @POST("channels.list")
    @FormUrlEncoded
    suspend fun getChannels(
        @Field("token") token: SlackToken,
        @Field("exclude_archived") excludeArchived: Boolean = true,
        @Field("exclude_members") excludeMembers: Boolean = true
    ): ChannelResponse
}