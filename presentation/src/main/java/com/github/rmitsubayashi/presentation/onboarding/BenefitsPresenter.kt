package com.github.rmitsubayashi.presentation.onboarding

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

class BenefitsPresenter(
    private val view: BenefitsContract.View
): BenefitsContract.Presenter {
    private val job: Job = Job()
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.IO

    override fun start() {
        view.hideAppBar()
    }

    override fun stop() {

    }

    override fun clickNext() {
        view.navigateToSlackToken()
    }
}