package com.github.rmitsubayashi.presentation.post

import com.github.rmitsubayashi.domain.interactor.HomeInteractor
import com.github.rmitsubayashi.domain.model.Recipient
import com.github.rmitsubayashi.presentation.post.model.RecipientType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

class SelectPostRecipientPresenter(
    private val view: SelectPostRecipientContract.View,
    private val homeInteractor: HomeInteractor
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
                RecipientType.USER -> homeInteractor.getUsers()
                RecipientType.CHANNEL -> homeInteractor.getChannels()
                RecipientType.THREAD -> homeInteractor.getRecentThreads()
            }
            withContext(Dispatchers.Main) {
                resource.data?.let {
                    view.setPostRecipients(it)
                }
            }
        }
    }

    override fun selectPostRecipient(recipient: Recipient) {
        view.navigateToPost(recipient)
    }
}