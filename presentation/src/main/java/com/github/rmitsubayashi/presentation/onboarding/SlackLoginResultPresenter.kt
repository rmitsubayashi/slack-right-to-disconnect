package com.github.rmitsubayashi.presentation.onboarding

import com.github.rmitsubayashi.domain.interactor.AuthenticationInteractor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

class SlackLoginResultPresenter(
    private val view: SlackLoginResultContract.View,
    private val authenticationInteractor: AuthenticationInteractor
) : SlackLoginResultContract.Presenter {
    private val job: Job = Job()
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.IO

    override fun start() {
        view.hideAppbar()
    }

    override fun stop() {
    }

    override fun clickFinish() {
        view.navigateToSelectType()
    }

    override fun receiveLoginResult(code: String?, state: String?) {
        if (code == null || state == null) {
            view.showGeneralError()
            return
        }
        view.showLoading(true)
        launch {
            val resource = authenticationInteractor.exchangeCodeForAccessToken(code, state)
            withContext(Dispatchers.Main) {
                view.showLoading(false)
                when (resource.error) {
                    null -> view.showSuccess()
                    else -> view.showGeneralError()
                }
            }
        }
    }

}