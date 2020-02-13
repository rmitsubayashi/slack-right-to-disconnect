package com.github.rmitsubayashi.presentation.post

import com.github.rmitsubayashi.domain.error.NetworkError
import com.github.rmitsubayashi.domain.interactor.HomeInteractor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

class PostPresenter(
    private val view: PostContract.View,
    private val homeInteractor: HomeInteractor
) : PostContract.Presenter {
    private val job: Job = Job()
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.IO

    override fun start() {

    }

    override fun stop() {

    }

    override fun setRecipient(id: String, threadID: String?) {
        homeInteractor.setRecipientID(id)
        homeInteractor.setThreadID(threadID)
    }

    override fun updateMessage(message: String) {
        homeInteractor.messageInputInteractor.updateInput(message)
    }

    override fun addMention(text: String, start: Int) {
        homeInteractor.messageInputInteractor.addMention(text, start)
    }

    override fun removeMention(text: String, start: Int) {
        homeInteractor.messageInputInteractor.removeMention(text, start)
    }

    override fun searchMentions(token: String, keyword: String) {
        launch {
            val matches = homeInteractor.messageInputInteractor.searchMention(keyword)
            withContext(Dispatchers.Main) {
                view.showMentionSuggestions(token, matches)
            }
        }
    }

    override fun postToSlack() {
        view.showPostSending()
        launch {
            val postResource = homeInteractor.post()
            withContext(Dispatchers.Main) {
                when (postResource.error) {
                    null -> view.navigateToPostSuccess()
                    NetworkError.RESOURCE_NOT_AVAILABLE -> view.showPostError("you have to set both your slack token and slack id")
                    else -> view.showPostError(postResource.error.toString())
                }
            }
        }
    }
}