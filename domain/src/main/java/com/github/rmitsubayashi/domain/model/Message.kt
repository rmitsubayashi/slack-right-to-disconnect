package com.github.rmitsubayashi.domain.model

import java.io.Serializable
import java.util.Date

data class Message (
    val message: String,
    val recipient: Recipient,
    val threadID: String?,
    val date: Date
): Serializable //pass between fragments