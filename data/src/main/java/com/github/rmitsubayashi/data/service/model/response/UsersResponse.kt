package com.github.rmitsubayashi.data.service.model.response

import com.google.gson.annotations.SerializedName

data class UsersResponse (
    @SerializedName("ok")
    val success: Boolean,
    @SerializedName("members")
    val users: List<User>,
    @SerializedName("error")
    val error: String
) {
    data class User (
        @SerializedName("id")
        val userID: String,
        @SerializedName("name")
        val username: String,
        @SerializedName("real_name")
        val realName: String,
        @SerializedName("deleted")
        val deleted: Boolean,
        @SerializedName("is_bot")
        val isBot: Boolean
    )
}