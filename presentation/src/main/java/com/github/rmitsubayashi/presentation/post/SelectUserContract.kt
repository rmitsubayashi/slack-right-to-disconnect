package com.github.rmitsubayashi.presentation.post

import com.github.rmitsubayashi.domain.model.Recipient
import com.github.rmitsubayashi.presentation.BasePresenter
import com.github.rmitsubayashi.presentation.BaseView

interface SelectUserContract {
    interface View: BaseView {
        fun navigateToPost(recipient: Recipient)
        fun setUsers(users: List<Recipient>)
        fun showNoNetwork()
        fun showTooManyUsers()
        fun showTooManySelectedUsers()
        fun setSelectButtonEnabled(enabled: Boolean)
        fun showLoading(loading: Boolean)
    }

    interface Presenter: BasePresenter {
        fun toggleUser(user: Recipient, selected: Boolean)
        fun selectUsers()
    }
}