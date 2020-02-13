package com.github.rmitsubayashi.data.model.response

import com.google.gson.annotations.SerializedName

data class ChannelResponse (
    @SerializedName("ok")
    val success: Boolean,
    @SerializedName("channels")
    val channels: List<ChannelResponseChannel>,
    @SerializedName("error")
    val error: String
)