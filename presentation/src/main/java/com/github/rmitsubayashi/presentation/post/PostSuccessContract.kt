package com.github.rmitsubayashi.presentation.post

import com.github.rmitsubayashi.domain.model.Recipient
import com.github.rmitsubayashi.presentation.BasePresenter
import com.github.rmitsubayashi.presentation.BaseView

interface PostSuccessContract {
    interface View: BaseView {
        fun navigateToSelectPostRecipientType()
        fun enableBookmarkButton(enable: Boolean)
    }

    interface Presenter: BasePresenter {
        fun bookmark(recipient: Recipient)
        fun checkBookmark(recipient: Recipient, threadID: String?)
    }
 }