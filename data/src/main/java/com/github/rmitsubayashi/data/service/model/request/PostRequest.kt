package com.github.rmitsubayashi.data.service.model.request

import com.google.gson.annotations.SerializedName

data class PostRequest (
    @SerializedName("text") val message: String,
    @SerializedName("channel") val channelID: String,
    @SerializedName("as_user") val asUser: Boolean = true,
    @SerializedName("thread_ts") val threadID: String? = null
)