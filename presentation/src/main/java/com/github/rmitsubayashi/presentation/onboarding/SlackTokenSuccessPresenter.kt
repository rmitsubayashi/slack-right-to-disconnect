package com.github.rmitsubayashi.presentation.onboarding

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

class SlackTokenSuccessPresenter(
    private val view: SlackTokenSuccessContract.View
) : SlackTokenSuccessContract.Presenter {
    private val job: Job = Job()
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.IO

    override fun start() {
    }

    override fun stop() {
    }

    override fun clickFinish() {
        view.navigateToSelectType()
    }

}