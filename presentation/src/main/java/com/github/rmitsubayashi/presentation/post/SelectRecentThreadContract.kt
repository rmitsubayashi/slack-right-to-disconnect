package com.github.rmitsubayashi.presentation.post

import com.github.rmitsubayashi.domain.model.Message
import com.github.rmitsubayashi.domain.model.Recipient
import com.github.rmitsubayashi.presentation.BasePresenter
import com.github.rmitsubayashi.presentation.BaseView

interface SelectRecentThreadContract {
    interface View: BaseView {
        fun navigateToPost(recipient: Recipient, threadID: String)
        fun setRecentThreads(recentThreads: List<Message>)
        fun showNoRecentThreads()
    }

    interface Presenter: BasePresenter {
        fun selectRecentThread(recentThread: Message)
    }
}