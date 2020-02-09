package com.github.rmitsubayashi.domain.model

data class UserInfo(
    override val id: String,
    override val name: String
) : Recipient