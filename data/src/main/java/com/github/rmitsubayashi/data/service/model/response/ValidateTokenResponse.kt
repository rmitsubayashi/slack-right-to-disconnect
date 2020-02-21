package com.github.rmitsubayashi.data.service.model.response

import com.google.gson.annotations.SerializedName

data class ValidateTokenResponse(
    @SerializedName("ok")
    val valid: Boolean,
    @SerializedName("team")
    val team: String,
    @SerializedName("user")
    val user: String
)