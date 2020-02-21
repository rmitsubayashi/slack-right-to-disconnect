package com.github.rmitsubayashi.data.service.model.response

import com.google.gson.annotations.SerializedName

data class PostResponse (
    @SerializedName("ok")
    val sent: Boolean,
    @SerializedName("error")
    val error: String,
    @SerializedName("ts")
    val threadIdentifier: String
)