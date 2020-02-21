package com.github.rmitsubayashi.data.service.model.response

import com.google.gson.annotations.SerializedName

data class UsersResponse (
    @SerializedName("ok")
    val success: Boolean,
    @SerializedName("members")
    val users: List<UsersResponseUser>,
    @SerializedName("error")
    val error: String
)