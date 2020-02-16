package com.github.rmitsubayashi.presentation.post

import com.github.rmitsubayashi.domain.model.Recipient
import com.github.rmitsubayashi.presentation.BasePresenter
import com.github.rmitsubayashi.presentation.BaseView
import com.github.rmitsubayashi.domain.model.RecipientType
import com.github.rmitsubayashi.domain.model.UserInfo

interface SelectPostRecipientContract {
    interface View: BaseView {
        fun navigateToPost(recipient: Recipient)
        fun setPostRecipients(recipients: List<Recipient>)
        fun showNoNetwork()
        fun showTooManySlackUsersOrChannels()
        fun showTooManySelectedUsers()
        fun setSelectButtonVisibility(visible: Boolean)
        fun setSelectButtonEnabled(enabled: Boolean)
    }

    interface Presenter: BasePresenter {
        fun loadPostRecipients(type: RecipientType)
        fun selectPostRecipient(recipient: Recipient)
        fun toggleUser(userInfo: UserInfo, selected: Boolean)
        fun selectUsers()
    }
}