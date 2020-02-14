package com.github.rmitsubayashi.domain.model

data class BookmarkedRecipient (
    override val id: String,
    override val name: String,
    val recipientType: RecipientType
): Recipient