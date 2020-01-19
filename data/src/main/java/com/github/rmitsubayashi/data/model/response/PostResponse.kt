package com.github.rmitsubayashi.data.model.response

import com.google.gson.annotations.SerializedName

data class PostResponse (
    @SerializedName("ok")
    val sent: Boolean,
    @SerializedName("error")
    val error: String
)