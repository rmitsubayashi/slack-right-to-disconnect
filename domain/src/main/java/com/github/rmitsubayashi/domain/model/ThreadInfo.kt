package com.github.rmitsubayashi.domain.model

import java.util.*

data class ThreadInfo(
    override val id: String,
    override val name: String,
    val date: Date,
    val threadID: String
): Recipient