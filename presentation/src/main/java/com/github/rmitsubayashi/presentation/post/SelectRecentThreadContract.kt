package com.github.rmitsubayashi.presentation.post

import com.github.rmitsubayashi.domain.model.Message
import com.github.rmitsubayashi.domain.model.Recipient
import com.github.rmitsubayashi.presentation.BasePresenter
import com.github.rmitsubayashi.presentation.BaseView

interface SelectRecentThreadContract {
    interface View: BaseView {
        fun navigateToPost(recipient: Recipient, message: Message)
        fun setRecentThreads(recentThreads: List<Message>)
        fun showNoRecentThreads(noRecentThreads: Boolean)
    }

    interface Presenter: BasePresenter {
        fun selectRecentThread(recentThread: Message)
    }
}