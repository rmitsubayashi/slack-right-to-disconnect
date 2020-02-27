package com.github.rmitsubayashi.presentation.onboarding

import com.github.rmitsubayashi.domain.interactor.AuthenticationInteractor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

class BenefitsPresenter(
    private val view: BenefitsContract.View,
    private val authenticationInteractor: AuthenticationInteractor
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
        val url = authenticationInteractor.getSlackLoginURL()
        view.openURL(url)
    }
}