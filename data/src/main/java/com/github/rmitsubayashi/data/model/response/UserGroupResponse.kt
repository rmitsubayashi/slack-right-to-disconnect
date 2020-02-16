package com.github.rmitsubayashi.data.model.response

import com.google.gson.annotations.SerializedName

data class UserGroupResponse (
    @SerializedName("ok")
    val success: Boolean,
    @SerializedName("channel")
    val channel: UserGroupResponseChannel,
    @SerializedName("error")
    val error: String
)