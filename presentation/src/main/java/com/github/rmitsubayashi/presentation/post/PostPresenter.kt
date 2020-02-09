package com.github.rmitsubayashi.presentation.post

import com.github.rmitsubayashi.domain.error.NetworkError
import com.github.rmitsubayashi.domain.interactor.HomeInteractor
import com.github.rmitsubayashi.domain.model.Message
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

    override fun setRecipientID(id: String) {
        homeInteractor.setRecipientID(id)
    }

    override fun updateMessage(message: String) {
        homeInteractor.updateMessage(Message(message))
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