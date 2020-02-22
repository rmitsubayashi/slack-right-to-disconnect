package com.github.rmitsubayashi.domain.model

import java.io.Serializable

data class Recipient (
    val slackID: String,
    val displayName: String,
    val recipientType: RecipientType
): Serializable //pass between fragments