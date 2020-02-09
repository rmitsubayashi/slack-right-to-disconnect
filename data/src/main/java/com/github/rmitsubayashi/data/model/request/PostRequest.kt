package com.github.rmitsubayashi.data.model.request

import com.github.rmitsubayashi.domain.model.Message
import com.google.gson.annotations.SerializedName

data class PostRequest (
    @SerializedName("text") val message: Message,
    @SerializedName("channel") val channelID: String,
    @SerializedName("as_user") val asUser: Boolean = true
)