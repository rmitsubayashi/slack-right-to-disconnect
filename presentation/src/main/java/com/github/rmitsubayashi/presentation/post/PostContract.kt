package com.github.rmitsubayashi.presentation.post

import com.github.rmitsubayashi.presentation.BasePresenter
import com.github.rmitsubayashi.presentation.BaseView

interface PostContract {
    interface View: BaseView {
        fun showPostSending()
        fun showPostError(errorMessage: String)
        fun navigateToPostSuccess()
    }

    interface Presenter: BasePresenter {
        fun setRecipient(id: String, threadID: String?)
        fun updateMessage(message: String)
        fun postToSlack()
    }
}