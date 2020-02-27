package com.github.rmitsubayashi.data.service.model.response

import com.google.gson.annotations.SerializedName

data class AccessTokenResponse(
    @SerializedName("ok")
    val success: Boolean,
    @SerializedName("access_token")
    val token: String,
    @SerializedName("error")
    val error: String
)