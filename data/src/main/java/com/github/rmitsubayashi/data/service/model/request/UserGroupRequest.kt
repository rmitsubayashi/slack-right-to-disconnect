package com.github.rmitsubayashi.data.service.model.request

import com.google.gson.annotations.SerializedName

data class UserGroupRequest(
    @SerializedName("users")
    val userList: String,
    @SerializedName("return_im")
    val returnAll: Boolean = false
)