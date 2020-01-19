package com.github.rmitsubayashi.presentation.home

import com.github.rmitsubayashi.domain.model.LateTime
import com.github.rmitsubayashi.domain.model.Message
import com.github.rmitsubayashi.domain.model.MessageTemplate
import com.github.rmitsubayashi.presentation.BasePresenter
import com.github.rmitsubayashi.presentation.BaseView

interface HomeContract {
    interface View: BaseView {
        fun setMessageTemplates(templates: List<MessageTemplate>)
        fun showEmptyMessageTemplatesError()
        fun showInputError()
        fun showConfirmMessage(message: Message)
        fun showPostSuccess()
        fun showPostSending()
        fun showPostError(errorMessage: String)
        fun navigateToSettings()
        fun openLateTimePicker()
        fun setLateTimeButtonText(text: String?)
        fun setPreview(text: String)
    }

    interface Presenter: BasePresenter {
        fun getMessageTemplates()
        fun updateLateTime(lateTime: LateTime)
        fun updateMessageTemplate(messageTemplate: MessageTemplate)
        fun postToSlack()
    }
}