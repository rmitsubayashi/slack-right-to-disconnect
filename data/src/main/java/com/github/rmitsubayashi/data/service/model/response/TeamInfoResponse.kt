package com.github.rmitsubayashi.data.service.model.response

import com.google.gson.annotations.SerializedName

data class TeamInfoResponse (
    @SerializedName("ok")
    val success: Boolean,
    @SerializedName("team")
    val team: Team
) {
    data class Team (
        @SerializedName("domain")
        val domain: String
    )
}