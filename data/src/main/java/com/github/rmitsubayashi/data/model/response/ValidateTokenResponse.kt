package com.github.rmitsubayashi.data.model.response

import com.google.gson.annotations.SerializedName

data class ValidateTokenResponse(
    @SerializedName("ok")
    val valid: Boolean,
    @SerializedName("error")
    val error: String?
)