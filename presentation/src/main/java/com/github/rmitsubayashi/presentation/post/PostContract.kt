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
        fun setRecipientID(id: String)
        fun updateMessage(message: String)
        fun postToSlack()
    }
}