package com.github.rmitsubayashi.presentation.settings

import com.github.rmitsubayashi.domain.model.MessageTemplate
import com.github.rmitsubayashi.presentation.BasePresenter
import com.github.rmitsubayashi.presentation.BaseView

interface MessageTemplateContract {
    interface View: BaseView {
        fun showNoLateTimeWarning()
        fun setMessageTemplates(messageTemplates: List<MessageTemplate>)
        fun showCouldNotLoadMessageTemplatesLoaded()
        fun showAddEditDialog(old: MessageTemplate?)
        fun showCouldNotSaveMessageTemplate()
    }

    interface Presenter: BasePresenter {
        fun loadMessageTemplates()
        fun setCurrentMessageTemplate(messageTemplate: MessageTemplate?)
        fun updateCurrentMessageTemplate(text: String)
        fun addCurrentMessageTemplate()
        fun removeCurrentMessageTemplate()
        fun editCurrentMessageTemplate()
        fun dismissNoLateTimeWarning()
        fun save()
    }
}