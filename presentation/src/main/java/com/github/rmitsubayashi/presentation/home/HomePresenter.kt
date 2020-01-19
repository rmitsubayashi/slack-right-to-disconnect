package com.github.rmitsubayashi.presentation.home

import com.github.rmitsubayashi.domain.error.NetworkError
import com.github.rmitsubayashi.domain.interactor.HomeInteractor
import com.github.rmitsubayashi.domain.model.LateTime
import com.github.rmitsubayashi.domain.model.MessageTemplate
import kotlinx.coroutines.*

class HomePresenter(
    private val homeView: HomeContract.View,
    private val homeInteractor: HomeInteractor
): HomeContract.Presenter {
    private val job: Job = Job()
    override val coroutineContext = job + Dispatchers.IO

    override fun start() {
        getMessageTemplates()
    }

    override fun stop() {
        job.cancel()
    }

    override fun getMessageTemplates() {
        launch {
            val reasonsResource = homeInteractor.loadMessageTemplates()
            withContext(Dispatchers.Main) {
                when (reasonsResource.error) {
                    null -> {
                        reasonsResource.data?.let { homeView.setMessageTemplates(it) } ?: homeView.showEmptyMessageTemplatesError()

                    } else -> {
                        homeView.showEmptyMessageTemplatesError()
                    }
                }
            }
        }
    }

    override fun updateMessageTemplate(messageTemplate: MessageTemplate) {
        val resultResource = homeInteractor.updateMessageTemplate(messageTemplate)
        if (resultResource.error == null) {
            homeView.setPreview(homeInteractor.getPreview())
        } else {
            homeView.showInputError()
        }
    }

    override fun updateLateTime(lateTime: LateTime) {
        val resultResource = homeInteractor.updateLateTime(lateTime)
        if (resultResource.error == null) {
            homeView.setLateTimeButtonText(lateTime.toString())
            homeView.setPreview(homeInteractor.getPreview())
        } else {
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