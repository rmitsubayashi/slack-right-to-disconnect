package com.github.rmitsubayashi.domain.model

data class SlackTokenInfo (
    val token: SlackToken,
    val team: String,
    val teamDomain: String,
    val user: String,
    val userID: String
)