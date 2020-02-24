package com.github.rmitsubayashi.slackrighttodisconnect.post.selectType

import com.github.rmitsubayashi.domain.model.Recipient

interface BookmarkListener {
    fun onBookmarkClicked(bookmarkedRecipient: Recipient)
    fun onBookmarkLongClicked(bookmarkedRecipient: Recipient)
}