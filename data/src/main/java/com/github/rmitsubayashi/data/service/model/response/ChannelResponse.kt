package com.github.rmitsubayashi.data.service.model.response

import com.google.gson.annotations.SerializedName

data class ChannelResponse (
    @SerializedName("ok")
    val success: Boolean,
    @SerializedName("channels")
    val channels: List<Channel>,
    @SerializedName("error")
    val error: String
) {
    data class Channel (
        @SerializedName("id")
        val channelID: String,
        @SerializedName("name")
        val name: String
    )
}