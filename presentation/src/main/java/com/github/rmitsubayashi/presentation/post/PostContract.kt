package com.github.rmitsubayashi.presentation.post

import com.github.rmitsubayashi.domain.model.Message
import com.github.rmitsubayashi.domain.model.Recipient
import com.github.rmitsubayashi.domain.model.RecipientType
import com.github.rmitsubayashi.presentation.BasePresenter
import com.github.rmitsubayashi.presentation.BaseView

interface PostContract {
    interface View: BaseView {
        fun showPostSending(sending: Boolean)
        fun showNoNetwork()
        fun showRestrictedChannel()
        fun showInvalidContent()
        fun navigateToPostSuccess()
        fun showMentionSuggestions(token: String, suggestions: List<String>)
        fun showRecipientInfo(recipient: Recipient)
        fun showRecentThreadInfo(message: Message, daysAgo: Int)
    }

    interface Presenter: BasePresenter {
        fun setRecipient(recipient: Recipient, thread: Message?)
        fun updateMessage(message: String)
        fun addMention(text: String, start: Int)
        fun removeMention(text: String, start: Int)
        fun searchMentions(token: String, keyword: String)
        fun postToSlack()
    }
}