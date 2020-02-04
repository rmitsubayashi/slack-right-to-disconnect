package com.github.rmitsubayashi.presentation.home

import com.github.rmitsubayashi.domain.error.NetworkError
import com.github.rmitsubayashi.domain.interactor.HomeInteractor
import com.github.rmitsubayashi.domain.model.Message
import kotlinx.coroutines.*

class HomePresenter(
    private val homeView: HomeContract.View,
    private val homeInteractor: HomeInteractor
): HomeContract.Presenter {
    private val job: Job = Job()
    override val coroutineContext = job + Dispatchers.IO

    override fun start() {
    }

    override fun stop() {
        job.cancel()
    }

    override fun updateMessage(message: String) {
        val resultResource = homeInteractor.updateMessage(Message(message))
        if (resultResource.error != null) {
            homeView.showInputError()
        }
    }


    override fun postToSlack() {
        homeView.showPostSending()
        launch {
            val postResource = homeInteractor.post()
            withContext(Dispatchers.Main) {
                when (postResource.error) {
                    null -> homeView.showPostSuccess()
                    NetworkError.RESOURCE_NOT_AVAILABLE -> homeView.showPostError("you have to set both your slack token and slack id")
                    else -> homeView.showPostError(postResource.error.toString())
                }
            }
        }
    }
}