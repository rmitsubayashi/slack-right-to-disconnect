package com.github.rmitsubayashi.presentation.post

import com.github.rmitsubayashi.domain.model.BookmarkedRecipient
import com.github.rmitsubayashi.domain.model.Recipient
import com.github.rmitsubayashi.presentation.BasePresenter
import com.github.rmitsubayashi.presentation.BaseView
import com.github.rmitsubayashi.domain.model.RecipientType

interface SelectPostRecipientTypeContract {
    interface View: BaseView {
        fun navigateToSelectPostRecipient(type: RecipientType)
        fun navigateToPost(recipient: BookmarkedRecipient)
        fun setBookmarks(bookmarks: List<BookmarkedRecipient>)
    }

    interface Presenter: BasePresenter {
        fun selectPostRecipientType(type: RecipientType)
        fun selectBookmark(bookmark: BookmarkedRecipient)
    }
}