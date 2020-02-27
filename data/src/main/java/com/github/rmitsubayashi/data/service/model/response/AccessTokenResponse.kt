package com.github.rmitsubayashi.data.service.model.response

import com.google.gson.annotations.SerializedName

data class AccessTokenResponse(
    @SerializedName("ok")
    val success: Boolean,
    @SerializedName("access_token")
    val token: String,
    @SerializedName("team_id")
    val teamID: String,
    @SerializedName("team_name")
    val teamName: String,
    @SerializedName("user_id")
    val userID: String,
    @SerializedName("error")
    val error: String
)