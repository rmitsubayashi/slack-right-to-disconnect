package com.github.rmitsubayashi.domain.model

import java.util.Date

data class Message (
    val message: String,
    val recipient: Recipient,
    val threadID: String?,
    val date: Date

)