package com.github.rmitsubayashi.data.service.model.response

import com.google.gson.annotations.SerializedName

data class ChannelResponseChannel (
    @SerializedName("id")
    val channelID: String,
    @SerializedName("name")
    val name: String
)