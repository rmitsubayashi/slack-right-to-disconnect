package com.github.rmitsubayashi.domain.model

import java.io.Serializable

data class Recipient (
    val slackID: String,
    val displayName: String,
    val recipientType: RecipientType
): Serializable //pass between fragments
{
    fun toSlackDisplayName(): String {
        return when (recipientType) {
            RecipientType.CHANNEL -> "#$displayName"
            RecipientType.USER -> {
                val userArray = displayName.split(", ")
                val slackDisplayUsers = userArray.map { "@$it" }
                slackDisplayUsers.joinToString(", ")
            }
        }
    }
}