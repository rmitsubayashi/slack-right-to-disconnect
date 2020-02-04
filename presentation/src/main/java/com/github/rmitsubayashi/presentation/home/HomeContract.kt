package com.github.rmitsubayashi.presentation.home

import com.github.rmitsubayashi.domain.model.Message
import com.github.rmitsubayashi.presentation.BasePresenter
import com.github.rmitsubayashi.presentation.BaseView

interface HomeContract {
    interface View: BaseView {
        fun showInputError()
        fun showConfirmMessage(message: Message)
        fun showPostSuccess()
        fun showPostSending()
        fun showPostError(errorMessage: String)
        fun navigateToSettings()
    }

    interface Presenter: BasePresenter {
        fun updateMessage(message: String)
        fun postToSlack()
    }
}