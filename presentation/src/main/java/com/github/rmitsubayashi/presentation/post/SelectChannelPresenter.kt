package com.github.rmitsubayashi.presentation.post

import com.github.rmitsubayashi.domain.error.NetworkError
import com.github.rmitsubayashi.domain.error.SlackError
import com.github.rmitsubayashi.domain.interactor.SelectChannelInteractor
import com.github.rmitsubayashi.domain.model.Recipient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

class SelectChannelPresenter(
    private val view: SelectChannelContract.View,
    private val selectChannelInteractor: SelectChannelInteractor
    ): SelectChannelContract.Presenter {
    private val job: Job = Job()
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.IO

    override fun start() {
        loadChannels()
    }

    private fun loadChannels() {
        view.showLoading(true)
        launch {
            val resource = selectChannelInteractor.getChannels()
            withContext(Dispatchers.Main){
                view.showLoading(false)
                when (resource.error) {
                    null -> resource.data?.let { view.setChannels(it) } ?: view.showGeneralError()
                    NetworkError.NOT_CONNECTED -> view.showNoNetwork()
                    SlackError.TOO_MANY_CHANNELS -> view.showTooManyChannels()
                    else -> view.showGeneralError()
                }
            }
        }
    }

    override fun stop() {
    }

    override fun selectChannel(channel: Recipient) {
        view.navigateToPost(channel)
    }
}