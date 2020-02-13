package com.github.rmitsubayashi.presentation.post

import com.github.rmitsubayashi.domain.model.Recipient
import com.github.rmitsubayashi.presentation.BasePresenter
import com.github.rmitsubayashi.presentation.BaseView
import com.github.rmitsubayashi.presentation.post.model.RecipientType

interface SelectPostRecipientContract {
    interface View: BaseView {
        fun navigateToPost(recipient: Recipient)
        fun setPostRecipients(recipients: List<Recipient>)
        fun showNoNetwork()
        fun showTooManySlackUsersOrChannels()
    }

    interface Presenter: BasePresenter {
        fun loadPostRecipients(type: RecipientType)
        fun selectPostRecipient(recipient: Recipient)
    }
}