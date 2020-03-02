package com.github.rmitsubayashi.presentation.onboarding

import com.github.rmitsubayashi.domain.interactor.AuthenticationInteractor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

class LoginRetryPresenter(
    val view: LoginRetryContract.View,
    val authenticationInteractor: AuthenticationInteractor
): LoginRetryContract.Presenter {
    private val job: Job = Job()
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.IO

    override fun start() {
        view.hideAppBar()
    }

    override fun stop() {

    }

    override fun clickRetry() {
        val clientID = view.getSlackClientID()
        val url = authenticationInteractor.getSlackLoginURL(clientID)
        view.openURL(url)
    }
}