package com.github.rmitsubayashi.data.service.model.response

import com.google.gson.annotations.SerializedName

data class UserGroupResponse (
    @SerializedName("ok")
    val success: Boolean,
    @SerializedName("channel")
    val channel: Channel,
    @SerializedName("error")
    val error: String
) {
    data class Channel (
        @SerializedName("id")
        val id: String
    )
}