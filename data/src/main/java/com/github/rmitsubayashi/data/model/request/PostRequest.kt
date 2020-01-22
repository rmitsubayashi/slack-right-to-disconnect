package com.github.rmitsubayashi.data.model.request

import com.github.rmitsubayashi.domain.model.Message
import com.github.rmitsubayashi.domain.model.SlackChannelID
import com.google.gson.annotations.SerializedName

data class PostRequest (
    @SerializedName("text") val message: Message,
    @SerializedName("channel") val channelID: SlackChannelID,
    @SerializedName("as_user") val asUser: Boolean = true
)