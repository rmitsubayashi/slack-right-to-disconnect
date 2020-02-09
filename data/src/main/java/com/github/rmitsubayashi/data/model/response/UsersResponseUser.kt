package com.github.rmitsubayashi.data.model.response

import com.google.gson.annotations.SerializedName

data class UsersResponseUser (
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