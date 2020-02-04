package com.github.rmitsubayashi.domain.model

data class SlackTokenInfo (
    val token: SlackToken,
    val team: String,
    val user: String
)