package com.github.rmitsubayashi.presentation.post

import com.github.rmitsubayashi.domain.model.Recipient
import com.github.rmitsubayashi.presentation.BasePresenter
import com.github.rmitsubayashi.presentation.BaseView

interface SelectTypeContract {
    interface View: BaseView {
        fun navigateToSelectUser()
        fun navigateToSelectChannel()
        fun navigateToSelectRecentThread()
        fun navigateToPost(recipient: Recipient)
        fun setBookmarks(bookmarks: List<Recipient>)
        fun removeBookmark(bookmark: Recipient)
        fun navigateToOnboarding()
    }

    interface Presenter: BasePresenter {
        fun selectSelectUser()
        fun selectSelectChannel()
        fun selectSelectRecentThread()
        fun selectBookmark(bookmark: Recipient)
        fun removeBookmark(bookmark: Recipient)
    }
}