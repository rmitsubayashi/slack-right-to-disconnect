package com.github.rmitsubayashi.presentation.post

import com.github.rmitsubayashi.domain.error.NetworkError
import com.github.rmitsubayashi.domain.error.SlackError
import com.github.rmitsubayashi.domain.error.ValidationError
import com.github.rmitsubayashi.domain.interactor.PostInteractor
import com.github.rmitsubayashi.domain.interactor.SelectRecentThreadInteractor
import com.github.rmitsubayashi.domain.model.Message
import com.github.rmitsubayashi.domain.model.Recipient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

class PostPresenter(
    private val view: PostContract.View,
    private val postInteractor: PostInteractor,
    private val selectRecentThreadInteractor: SelectRecentThreadInteractor
) : PostContract.Presenter {
    private val job: Job = Job()
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.IO

    override fun start() {

    }

    override fun stop() {

    }

    override fun setRecipient(recipient: Recipient, thread: Message?) {
        postInteractor.setRecipient(recipient)
        postInteractor.setThreadID(thread?.threadID)
        if (thread != null) {
            val diff = selectRecentThreadInteractor.getDaysAgo(thread)
            view.showRecentThreadInfo(thread, diff)
        } else {
            view.showRecipientInfo(recipient)
        }
    }

    override fun updateMessage(message: String) {
        postInteractor.messageInputInteractor.updateInput(message)
    }

    override fun addMention(text: String, start: Int) {
        postInteractor.messageInputInteractor.addMention(text, start)
    }

    override fun removeMention(text: String, start: Int) {
        postInteractor.messageInputInteractor.removeMention(text, start)
    }

    override fun searchMentions(token: String, keyword: String) {
        launch {
            val matches = postInteractor.messageInputInteractor.searchMention(keyword)
            withContext(Dispatchers.Main) {
                view.showMentionSuggestions(token, matches)
            }
        }
    }

    override fun postToSlack() {
        view.showPostSending(true)
        launch {
            val postResource = postInteractor.post()
            withContext(Dispatchers.Main) {
                view.showPostSending(false)
                when (postResource.error) {
                    null -> view.navigateToPostSuccess()
                    SlackError.RESTRICTED_CHANNEL -> view.showRestrictedChannel()
                    NetworkError.NOT_CONNECTED -> view.showNoNetwork()
                    ValidationError.INVALID_CONTENT -> view.showInvalidContent()
                    else -> view.showGeneralError()
                }
            }
        }
    }
}