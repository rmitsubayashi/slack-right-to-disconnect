package com.github.rmitsubayashi.data.model.request

import com.github.rmitsubayashi.domain.model.SlackToken
import com.google.gson.annotations.SerializedName

data class UserGroupRequest(
    @SerializedName("users")
    val userList: String,
    @SerializedName("return_im")
    val returnAll: Boolean = false
)