package com.github.rmitsubayashi.presentation.post

import com.github.rmitsubayashi.presentation.BasePresenter
import com.github.rmitsubayashi.presentation.BaseView
import com.github.rmitsubayashi.presentation.post.model.RecipientType

interface SelectPostRecipientTypeContract {
    interface View: BaseView {
        fun navigateToSelectPostRecipient(type: RecipientType)
    }

    interface Presenter: BasePresenter {
        fun selectPostRecipientType(type: RecipientType)
    }
}