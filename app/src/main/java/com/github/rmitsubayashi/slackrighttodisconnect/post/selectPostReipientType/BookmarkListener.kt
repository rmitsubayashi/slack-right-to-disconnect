package com.github.rmitsubayashi.slackrighttodisconnect.post.selectPostReipientType

import com.github.rmitsubayashi.domain.model.BookmarkedRecipient

interface BookmarkListener {
    fun onBookmarkClicked(bookmarkedRecipient: BookmarkedRecipient)
}