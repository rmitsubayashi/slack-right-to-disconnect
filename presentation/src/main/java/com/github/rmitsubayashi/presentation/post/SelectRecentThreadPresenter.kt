package com.github.rmitsubayashi.presentation.post

import com.github.rmitsubayashi.domain.interactor.SelectRecentThreadInteractor
import com.github.rmitsubayashi.domain.model.Message
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

class SelectRecentThreadPresenter(
    private val view: SelectRecentThreadContract.View,
    private val selectRecentThreadInteractor: SelectRecentThreadInteractor
): SelectRecentThreadContract.Presenter {
    private val job: Job = Job()
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.IO

    override fun start() {
        loadRecentThreads()
    }

    private fun loadRecentThreads() {
        launch {
            val resource = selectRecentThreadInteractor.getRecentThreads()
            withContext(Dispatchers.Main){
                when (resource.error) {
                    null -> resource.data?.let {
                        if (it.isEmpty()) {
                            view.showNoRecentThreads()
                        } else {
                            view.setRecentThreads(it)
                        }
                    } ?: view.showGeneralError()
                    else -> view.showGeneralError()
                }
            }
        }
    }

    override fun stop() {
    }

    override fun selectRecentThread(recentThread: Message) {
        view.navigateToPost(recentThread.recipient, recentThread)
    }
}