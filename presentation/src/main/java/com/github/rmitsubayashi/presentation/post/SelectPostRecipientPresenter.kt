package com.github.rmitsubayashi.presentation.post

import com.github.rmitsubayashi.domain.error.NetworkError
import com.github.rmitsubayashi.domain.error.SlackError
import com.github.rmitsubayashi.domain.interactor.RecipientInteractor
import com.github.rmitsubayashi.domain.model.Recipient
import com.github.rmitsubayashi.domain.model.RecipientType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

class SelectPostRecipientPresenter(
    private val view: SelectPostRecipientContract.View,
    private val recipientInteractor: RecipientInteractor
): SelectPostRecipientContract.Presenter {
    private val job: Job = Job()
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.IO

    override fun start() {
    }

    override fun stop() {
    }

    override fun loadPostRecipients(type: RecipientType) {
        launch {
            val resource = when (type) {
                RecipientType.USER -> recipientInteractor.getUsers()
                RecipientType.CHANNEL -> recipientInteractor.getChannels()
                RecipientType.THREAD -> recipientInteractor.getRecentThreads()
            }
            withContext(Dispatchers.Main) {
                when (resource.error) {
                    null -> {
                        resource.data?.let {
                            view.setPostRecipients(it)
                        } ?: view.showGeneralError()
                    }
                    NetworkError.NOT_CONNECTED -> view.showNoNetwork()
                    SlackError.TOO_MANY_USERS, SlackError.TOO_MANY_CHANNELS -> view.showTooManySlackUsersOrChannels()
                    else -> view.showGeneralError()
                }

            }
        }
    }

    override fun selectPostRecipient(recipient: Recipient) {
        view.navigateToPost(recipient)
    }
}