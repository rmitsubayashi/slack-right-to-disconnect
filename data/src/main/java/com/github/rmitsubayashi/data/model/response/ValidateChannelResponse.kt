package com.github.rmitsubayashi.data.model.response

import com.google.gson.annotations.SerializedName

data class ValidateChannelResponse(
    @SerializedName("ok")
    val exists: Boolean,
    @SerializedName("error")
    val error: String
)