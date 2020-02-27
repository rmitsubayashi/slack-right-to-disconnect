package com.github.rmitsubayashi.data.service.model.response

import com.google.gson.annotations.SerializedName

data class UserInfoResponse (
    @SerializedName("ok")
    val success: Boolean,
    @SerializedName("user")
    val user: User,
    @SerializedName("error")
    val error: String
) {
    data class User (
        @SerializedName("name")
        val slackName: String,
        @SerializedName("real_name")
        val displayName: String
    )
}