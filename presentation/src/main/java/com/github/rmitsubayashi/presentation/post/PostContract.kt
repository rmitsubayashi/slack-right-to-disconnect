package com.github.rmitsubayashi.presentation.post

import com.github.rmitsubayashi.domain.model.Recipient
import com.github.rmitsubayashi.domain.model.RecipientType
import com.github.rmitsubayashi.presentation.BasePresenter
import com.github.rmitsubayashi.presentation.BaseView

interface PostContract {
    interface View: BaseView {
        fun showPostSending()
        fun showNoNetwork()
        fun showRestrictedChannel()
        fun showInvalidContent()
        fun navigateToPostSuccess()
        fun showMentionSuggestions(token: String, suggestions: List<String>)
    }

    interface Presenter: BasePresenter {
        fun setRecipient(recipient: Recipient, threadID: String?)
        fun updateMessage(message: String)
        fun addMention(text: String, start: Int)
        fun removeMention(text: String, start: Int)
        fun searchMentions(token: String, keyword: String)
        fun postToSlack()
    }
}